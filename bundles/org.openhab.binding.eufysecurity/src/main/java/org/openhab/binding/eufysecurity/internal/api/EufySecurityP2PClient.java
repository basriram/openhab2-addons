/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.eufysecurity.internal.api;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.eufysecurity.internal.api.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The milightV6 protocol is stateful and needs an established session for each client.
 * This class handles the password bytes, session bytes and sequence number.
 *
 * The session handshake is a 3-way handshake. First we are sending either a general
 * search for bridges command or a search for a specific bridge command (containing the bridge ID)
 * with our own client session bytes included.
 *
 * The response will assign as session bytes that we can use for subsequent commands
 * see {@link MilightV6SessionManager#sid1} and see {@link MilightV6SessionManager#sid2}.
 *
 * We register ourself to the bridge now and finalise the handshake by sending a register command
 * see {@link MilightV6SessionManager#sendRegistration()} to the bridge.
 *
 * From this point on we are required to send keep alive packets to the bridge every ~10sec
 * to keep the session alive. Because each command we send is confirmed by the bridge, we know if
 * our session is still valid and can redo the session handshake if necessary.
 *
 * @author David Graeff - Initial contribution
 */
@NonNullByDefault
public class EufySecurityP2PClient implements Runnable, Closeable {
    protected final Logger logger = LoggerFactory.getLogger(EufySecurityP2PClient.class);

    // The used sequence number for a command will be present in the response of the iBox. This
    // allows us to identify failed command deliveries.
    private int sequenceNo = 0;

    // Password bytes 1 and 2
    public byte pw[] = { 0, 0 };

    // Session bytes 1 and 2
    public byte sid[] = { 0, 0 };

    // Client session bytes 1 and 2. Those are fixed for now.
    public final byte clientSID1 = (byte) 0xab;
    public final byte clientSID2 = (byte) 0xde;


    /**
     * The session handshake is a 3 way handshake.
     */
    public enum SessionState {
        // No session established and nothing in progress
        SESSION_INVALID,
        // Send "find bridge" and wait for response
        SESSION_WAIT_FOR_BRIDGE,
        // Send "get session bytes" and wait for response
        SESSION_WAIT_FOR_SESSION_SID,
        // Session bytes received, register session now
        SESSION_NEED_REGISTER,
        // Registration complete, session is valid now
        SESSION_VALID,
        // The session is still active, a keep alive was just received.
        SESSION_VALID_KEEP_ALIVE,
    }

    public enum StateMachineInput {
        NO_INPUT,
        TIMEOUT,
        INVALID_COMMAND,
        KEEP_ALIVE_RECEIVED,
        BRIDGE_CONFIRMED,
        SESSION_ID_RECEIVED,
        SESSION_ESTABLISHED,
    }

    private SessionState sessionState = SessionState.SESSION_INVALID;

    // Implement this interface to get notifications about the current session state.
    public interface ISessionState {
        /**
         * Notifies about a state change of {@link MilightV6SessionManager}.
         * SESSION_VALID_KEEP_ALIVE will be reported in the interval, given to the constructor of
         * {@link MilightV6SessionManager}.
         *
         * @param state   The new state
         * @param address The remote IP address. Only guaranteed to be non null in the SESSION_VALID* states.
         */
        void sessionStateChanged(SessionState state, @Nullable InetAddress address, int port);
    }

    private final ISessionState observer;

    /** Used to determine if the session needs a refresh */
    private Instant lastSessionConfirmed = Instant.now();
    /** Quits the receive thread if set to true */
    private volatile boolean willbeclosed = false;
    /** Keep track of send commands and their sequence number */
    private final Map<Integer, Instant> usedSequenceNo = new TreeMap<>();
    /** The receive thread for all bridge responses. */
    private final Thread sessionThread;

    private final Station station;
    private byte[] cameraDID; //SECCAMA 00 00 00 00 00 XYZAB
    private @Nullable DatagramSocket datagramSocket;
    private @Nullable CompletableFuture<DatagramSocket> startFuture;

    /**
     * Usually we only send BROADCAST packets. If we know the IP address of the bridge though,
     * we should try UNICAST packets before falling back to BROADCAST.
     * This allows communication with the bridge even if it is in another subnet.
     */
    private @Nullable final InetAddress destIP;
    /**
     * We cache the last known IP to avoid using broadcast.
     */
    private @Nullable InetAddress lastKnownIP;
    private int lastKnownPort;

    private @Nullable InetAddress lastKnownStunIP;
    private int lastKnownStunPort;

    private final int port;

    /** The maximum duration for a session registration / keep alive process in milliseconds. */
    public static final int TIMEOUT_MS = 31000;
    /** A packet is handled as lost / not confirmed after this time */
    public static final int MAX_PACKET_IN_FLIGHT_MS = 20000;
    /** The keep alive interval. Must be between 100 and REG_TIMEOUT_MS milliseconds or 0 */
    private final int keepAliveInterval;

    /**
     * A session manager for the V6 bridge needs a way to send data (a QueuedSend object), the destination bridge ID, a
     * scheduler for timeout timers and optionally an observer for session state changes.
     *
     * @param sendQueue A send queue. Never remove or change that object while the session manager is still working.
     * @param station Destination bridge ID. If the bridge ID for whatever reason changes, you need to create a new
     *            session manager object
     * @param scheduler A framework scheduler to create timeout events.
     * @param observer Get notifications of state changes
     * @param destIP If you know the bridge IP address, provide it here.
     * @param port The bridge port
     * @param keepAliveInterval The keep alive interval. Must be between 100 and REG_TIMEOUT_MS milliseconds.
     *            if it is equal to REG_TIMEOUT_MS, then a new session will be established instead of renewing the
     *            current one.
     * @param pw The two "password" bytes for the bridge
     */
    public EufySecurityP2PClient(Station station, ISessionState observer) {
        this.station = station;
        this.observer = observer;
       this.destIP = null;
        this.lastKnownIP = null;
       this.port = 0;
        this.keepAliveInterval = 30000;
        this.cameraDID = new byte[] { 0x0 };
        if (!parseDID(station)) {
            throw new IllegalArgumentException("Station does not contain valid P2P_DID");
        }
        sessionThread = new Thread(this, "SessionThread");
    }

    /**
     * Start the session thread if it is not already running
     */
    public CompletableFuture<DatagramSocket> start() {
        if (willbeclosed) {
            CompletableFuture<DatagramSocket> f = new CompletableFuture<>();
            f.completeExceptionally(null);
            return f;
        }
        if (sessionThread.isAlive()) {
            logger.debug("Inside session thread is alive");
            DatagramSocket s = datagramSocket;
            assert s != null;
            logger.debug("Invoking completeableFeaute with socket");
            return CompletableFuture.completedFuture(s);
        }

        CompletableFuture<DatagramSocket> f = new CompletableFuture<>();
        startFuture = f;
        sessionThread.start();
        return f;
    }

    /**
     * You have to call that if you are done with this object. Cleans up the receive thread.
     */
    @Override
    public void close() throws IOException {
        if (willbeclosed) {
            return;
        }
        willbeclosed = true;
        final DatagramSocket socket = datagramSocket;
        if (socket != null) {
            socket.close();
        }
        sessionThread.interrupt();
        try {
            sessionThread.join();
        } catch (InterruptedException e) {
        }
    }

    public Instant getLastSessionValidConfirmation() {
        return lastSessionConfirmed;
    }

    private boolean parseDID(Station station) {
        String p2pDID = station.p2p_did;
        logger.debug("Station P2PDID is {}", p2pDID);
        ByteArrayBuilder builder = new ByteArrayBuilder();
        StringTokenizer st = new StringTokenizer(p2pDID, "-", false);
        if (st.countTokens() != 3) {
            logger.warn("P2P DID is not valid format. Expected <STR>-<NUM>-<STR>. Found {}", p2pDID);
            builder.close();
            this.cameraDID = new byte[] { 0x0 };
            return false;
        }
        builder.write(st.nextToken().getBytes());
        builder.write(0x0);
        long numericVal = Long.parseLong(st.nextToken());
        byte[] val = new byte[4];
        for (int i = 3; i >= 0; i--) {
            val[i] = (byte) (numericVal & 0xFF);
            numericVal >>= 8;
        }
        builder.write(val);
        builder.write(st.nextToken().getBytes());
        builder.close();
        this.cameraDID = builder.toByteArray();
        logger.debug("Parsed DID,{}", this.cameraDID);
        logUnknownPacket(this.cameraDID, this.cameraDID.length, "Parsed DID is ");
        return true;
    }
    
    /**
     * Send a search for bridgeID packet on all network interfaces.
     * This is used for the initial way to determine the IP of the bridge as well
     * as if the IP of a bridge has changed and the session got invalid because of that.
     *
     * A response will assign us session bytes.
     *
     * @throws InterruptedException
     */
    @SuppressWarnings({ "null", "unused" })
    private void sendSearchForBroadcast(DatagramSocket datagramSocket) {
        logger.warn("starting search for broadcast");
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.write(0xf1);
        builder.write(0x00);
        builder.write(0x00);
        builder.write(0x00); // length of remaining
        builder.close();
        byte[] t = builder.toByteArray();
        logger.debug("STUN Hello request bytes []", t);
        logUnknownPacket(t, t.length, "Sending STUN Hello request");

        for (String stunServer : EufySecurityConstants.STUN_SERVERS) {
            logger.debug("STUN Hello request to Server {}", stunServer);
            try {
                InetAddress stunAddr = InetAddress.getByName(stunServer);
                DatagramPacket packet = new DatagramPacket(t, t.length, stunAddr, EufySecurityConstants.STUN_PORT);
                datagramSocket.send(packet);
                logger.debug("Successfully sent STUN Hello Request Packet to stun server {},{}", stunServer, toString());
            } catch (IOException ie) {
                logger.warn("Could not send STUN Hello Request Packet to Stun Server {}, {}", stunServer,
                        ie.getLocalizedMessage());
                sessionState = SessionState.SESSION_INVALID;
            }

        }
    }    

    /**
     * Send a search for bridgeID packet on all network interfaces.
     * This is used for the initial way to determine the IP of the bridge as well
     * as if the IP of a bridge has changed and the session got invalid because of that.
     *
     * A response will assign us session bytes.
     *
     * @throws InterruptedException
     */
    @SuppressWarnings({ "null", "unused" })
    private void sendUIDLookupRequest(DatagramSocket datagramSocket, InetAddress stunIp) {
        logger.warn("starting UID Lookup request");
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.write(0xf1);
        builder.write(0x20);
        builder.write(0x00);
        builder.write(0x24); // length of remaining
        builder.write(this.cameraDID);
        builder.write(0x00);
        builder.write(0x00);
        builder.write(0x00);
        builder.write(0x00);
        builder.write(0x02);
        int localPort = datagramSocket.getLocalPort();
        logger.debug("Local port is {}", localPort);
        builder.write((byte) (localPort & 0xFF));
        builder.write((byte) (localPort >> 8));
        //byte[] localIp = datagramSocket.getLocalAddress().getAddress();
        byte[] localIp = new byte[] { (byte) 0x46, (byte) 0x1e, (byte) 0xa8, (byte) 0xc0 };
        builder.write(localIp);
      //  for (int i = 0; i <= 3; i++) {
      //      builder.write((byte)localIp[i]));
     //      // builder.write((byte) (localIp[i] >> 8));
     //   }
        for (int i = 0; i <= 7; i++) {
            builder.write((byte) 0);
        }
        builder.close();
        byte[] t = builder.toByteArray();
        logger.debug("UID request bytes []", t);
        logUnknownPacket(t, t.length, "Sending UID lookup request");
        logger.debug("UID Request to STUN Server {}", stunIp);
        try {
            DatagramPacket packet = new DatagramPacket(t, t.length, stunIp, EufySecurityConstants.STUN_PORT);
            datagramSocket.send(packet);
            logger.debug("Successfully sent UID Request Packet to stun server {},{}", stunIp, toString());
        } catch (IOException ie) {
            logger.warn("Could not send UID Request Packet to Stun Server {}, {}", stunIp,
                    ie.getLocalizedMessage());
            sessionState = SessionState.SESSION_INVALID;
        }
    }


    @SuppressWarnings({ "null", "unused" })
    private void sendProbeRequest(DatagramSocket datagramSocket, InetAddress localCameraIp, int localCameraPort) {
        logger.warn("starting camera probe request");
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.write(0xf1);
        builder.write(0x41);
        builder.write(0x00);
        builder.write(0x14); // length of remaining
        builder.write(this.cameraDID);
        builder.write(0x00);
        builder.write(0x00);
        builder.write(0x00);
        builder.close();
        byte[] t = builder.toByteArray();
        logger.debug("UID request bytes []", t);
        logUnknownPacket(t, t.length, "Sending UID lookup request");
        try {
            DatagramPacket packet = new DatagramPacket(t, t.length, localCameraIp, localCameraPort);
            datagramSocket.send(packet);
            logger.debug("Successfully sent Camera Probe Request Packet to {},{}", localCameraIp, t.toString());
        } catch (IOException ie) {
            logger.warn("Could not send camera probe request to {}, {}", localCameraIp, ie.getLocalizedMessage());
            sessionState = SessionState.SESSION_INVALID;
        }
    }
    
    @SuppressWarnings({ "null", "unused" })
    private void sendKeepAlive(DatagramSocket datagramSocket, InetAddress localCameraIp, int localCameraPort) {
        logger.warn("sending keep alive");
        /*        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.write(0xf1);
        builder.write(0xe0);
        builder.write(0x00);
        builder.write(0x00); // length of remaining
        builder.close();
        byte[] t = builder.toByteArray();
        logUnknownPacket(t, t.length, "Sending keep alive request");
        */
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.write(0xf1);
        builder.write(0x41);
        builder.write(0x00);
        builder.write(0x14); // length of remaining
        builder.write(this.cameraDID);
        builder.write(0x00);
        builder.write(0x00);
        builder.write(0x00);
        builder.close();
        byte[] t = builder.toByteArray();        

        try {
            DatagramPacket packet = new DatagramPacket(t, t.length, localCameraIp, localCameraPort);
            datagramSocket.send(packet);
            logger.debug("Successfully sent keep alive to {}:{},{}", localCameraIp, localCameraPort, t.toString());
        } catch (IOException ie) {
            logger.warn("Could not send keep alive to {}:{}, {}", localCameraIp, localCameraPort,
                    ie.getLocalizedMessage());
            sessionState = SessionState.SESSION_INVALID;
        }

    }    


    /**
     * Constructs a 0x80... command which us used for all colour,brightness,saturation,mode operations.
     * The session ID, password and sequence number is automatically inserted from this object.
     *
     * Produces data like:
     * SN: Sequence number
     * S1: SessionID1
     * S2: SessionID2
     * P1/P2: Password bytes
     * WB: Remote (08) or iBox integrated bulb (00)
     * ZN: Zone {Zone1-4 0=All}
     * CK: Checksum
     *
     * #zone 1 on
     * @ 80 00 00 00 11 84 00 00 0c 00 31 00 00 08 04 01 00 00 00 01 00 3f
     *
     * Colors:
     * CC: Color value (hue)
     * 80 00 00 00 11 S1 S2 SN SN 00 31 P1 P2 WB 01 CC CC CC CC ZN 00 CK
     *
     * 80 00 00 00 11 D4 00 00 12 00 31 00 00 08 01 FF FF FF FF 01 00 38
     *
     * @return
     */
    public byte[] makeCommand(byte wb, int zone, int... data) {

        return new byte[]{0x0};
    }

    /**
     * Constructs a 0x3D or 0x3E link/unlink command.
     * The session ID, password and sequence number is automatically inserted from this object.
     *
     * WB: Remote (08) or iBox integrated bulb (00)
     */
    public byte[] makeLink(byte wb, int zone, boolean link) {

        return new byte[]{0x0};
    }

    /**
     * The main state machine of the session handshake.
     *
     * @throws InterruptedException
     * @throws IOException
     */
    private void sessionStateMachine(DatagramSocket datagramSocket, StateMachineInput input) throws IOException {
        final SessionState lastSessionState = sessionState;
        logStateMachine(input);
        // Check for timeout
        final Instant current = Instant.now();
        final Duration timeElapsed = Duration.between(lastSessionConfirmed, current);
        if (timeElapsed.toMillis() > TIMEOUT_MS) {
            if (sessionState != SessionState.SESSION_WAIT_FOR_BRIDGE) {
                logger.warn("Session timeout!");
            }
            // One reason we failed, might be that a last known IP is not correct anymore.
            // Reset to the given dest IP (which might be null).
           // lastKnownIP = destIP;
           // sessionState = SessionState.SESSION_INVALID;
        }

        if (input == StateMachineInput.INVALID_COMMAND) {
            sessionState = SessionState.SESSION_INVALID;
        }

        final @NonNull InetAddress myaddr = (@NonNull InetAddress) lastKnownIP;
        final @NonNull InetAddress stunaddr = (@NonNull InetAddress) lastKnownStunIP;

        switch (sessionState) {
            case SESSION_INVALID:
                usedSequenceNo.clear();
                sessionState = SessionState.SESSION_WAIT_FOR_BRIDGE;
                lastSessionConfirmed = Instant.now();
            case SESSION_WAIT_FOR_BRIDGE:
                if (input == StateMachineInput.BRIDGE_CONFIRMED) {
                    sessionState = SessionState.SESSION_WAIT_FOR_SESSION_SID;
                } else {
                    datagramSocket.setSoTimeout(150);
                    sendSearchForBroadcast(datagramSocket);
                    break;
                }
            case SESSION_WAIT_FOR_SESSION_SID:
                if (input == StateMachineInput.SESSION_ID_RECEIVED) {
                    logger.debug("Session ID received: {}", String.format("%02X %02X", this.sid[0], this.sid[1]));
                    sessionState = SessionState.SESSION_NEED_REGISTER;
                } else {
                    datagramSocket.setSoTimeout(300);
                    sendUIDLookupRequest(datagramSocket, stunaddr);
                    break;
                }
            case SESSION_NEED_REGISTER:
                if (input == StateMachineInput.SESSION_ESTABLISHED) {
                    sessionState = SessionState.SESSION_VALID;
                    lastSessionConfirmed = Instant.now();
                    logger.debug("Registration complete");
                     sendKeepAlive(datagramSocket, myaddr, lastKnownPort);
    
                } else {
                    datagramSocket.setSoTimeout(300);
                    sendProbeRequest(datagramSocket, myaddr, lastKnownPort);
                    break;
                }
            case SESSION_VALID_KEEP_ALIVE:
            case SESSION_VALID:
                if (input == StateMachineInput.KEEP_ALIVE_RECEIVED) {
                    lastSessionConfirmed = Instant.now();
                    observer.sessionStateChanged(SessionState.SESSION_VALID_KEEP_ALIVE, myaddr, lastKnownPort);
                } else {
                    final InetAddress address = lastKnownIP;
                    if (keepAliveInterval > 0 && timeElapsed.toMillis() > keepAliveInterval && address != null) {
                        sendKeepAlive(datagramSocket, myaddr, lastKnownPort);
                    }
                    // Increase socket timeout to wake up for the next keep alive interval
                    datagramSocket.setSoTimeout(keepAliveInterval);
                }
                break;
        }
        logStateMachine(input);
        if (lastSessionState != sessionState) {
            observer.sessionStateChanged(sessionState, myaddr, lastKnownPort);
        }
    }

    private void logUnknownPacket(byte[] data, int len, String reason) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            s.append(String.format("%02X ", data[i]));
        }
        logger.info("{} ({}): {}", reason, station.p2p_did, s);
    }

    /**
     * The session thread executes this run() method and a blocking UDP receive
     * is performed in a loop.
     */
    @SuppressWarnings({ "null", "unused" })
    @Override
    public void run() {
        try (DatagramSocket datagramSocket = new DatagramSocket(null)) {
            this.datagramSocket = datagramSocket;
            datagramSocket.setBroadcast(true);
            datagramSocket.setReuseAddress(true);
            datagramSocket.setSoTimeout(150);
            datagramSocket.bind(null);

            logger.debug("MilightCommunicationV6 receive thread ready");

            // Inform the start future about the datagram socket
            CompletableFuture<DatagramSocket> f = startFuture;
            if (f != null) {
                f.complete(datagramSocket);
                startFuture = null;
            }

            byte[] buffer = new byte[1024];
            DatagramPacket rPacket = new DatagramPacket(buffer, buffer.length);

            sessionStateMachine(datagramSocket, StateMachineInput.NO_INPUT);

            // Now loop forever, waiting to receive packets and printing them.
            while (!willbeclosed) {
                rPacket.setLength(buffer.length);
                try {
                    datagramSocket.receive(rPacket);
                } catch (SocketTimeoutException e) {
                    sessionStateMachine(datagramSocket, StateMachineInput.TIMEOUT);
                    continue;
                }
                int len = rPacket.getLength();

                if (len < 3) {
                    logUnknownPacket(buffer, len, "Not an iBox response!");
                    continue;
                }

                int expectedLen = buffer[3] + 4;
                if (expectedLen > len) {
                    logUnknownPacket(buffer, len, "Unexpected size!");
                    continue;
                } 
                byte[] ipaddr = new byte[4];
                DatagramPacket echo1Packet;
                switch (buffer[1]) {
                    // 13 00 00 00 0A 03 D3 54 11 (AC CF 23 F5 7A D4)
                    case (byte) 0x01:  // STUN Hello Response
                        logger.debug("Received Stun Hello");
                        this.lastKnownStunIP = rPacket.getAddress();
                        logger.debug("Valid STUN Server ip {}", this.lastKnownStunIP);
                        sessionStateMachine(datagramSocket, StateMachineInput.BRIDGE_CONFIRMED);
                        break;
                    case (byte) 0x21: // UID ack
                        logger.debug("Received UID Request ACK ");
                        break;
                    case (byte) 0x40:  // UID Response
                        logger.debug("Received UID Response");
                        // STUN UID Lookup Response
                        // f1 40 00 10 00 02 9d 36  8d ce b8 2f 00 00 00 00 00 00 00 00
                        ipaddr[0] = buffer[11];
                        ipaddr[1] = buffer[10];
                        ipaddr[2] = buffer[9];
                        ipaddr[3] = buffer[8];
                        lastKnownIP = InetAddress.getByAddress(ipaddr);
                        lastKnownPort = (buffer[7]*256+buffer[6]);
                        logger.warn("Ip address is :" + lastKnownIP.toString() + ":" + lastKnownPort);
                        sessionStateMachine(datagramSocket, StateMachineInput.SESSION_ID_RECEIVED);
                         break;
                    case (byte) 0x41:  // UID Request from device
                        logger.debug("Received UID Request from device");
                        // STUN UID Lookup Response
                        // f1 41 00 10 00 02 9d 36  8d ce b8 2f 00 00 00 00 00 00 00 00
                        logger.debug("Switching from known local port {} to this one {}", lastKnownPort,
                                rPacket.getPort());
                        this.lastKnownPort = rPacket.getPort();
                        DatagramPacket echoPacket = new DatagramPacket(rPacket.getData(), rPacket.getLength(), lastKnownIP,
                                lastKnownPort);
                        logUnknownPacket(rPacket.getData(), rPacket.getLength(), "sending echo back");
                        logger.debug("Sending echo back to {}.{}", lastKnownIP, lastKnownPort);
                        datagramSocket.send(echoPacket); // echo back  
                        sessionStateMachine(datagramSocket, StateMachineInput.SESSION_ESTABLISHED);
                        break;
                        case (byte) 0x42:  // session established
                        logger.debug("Received received SESSION ESTABLISHED from device");
                        // STUN UID Lookup Response
                        // f1 41 00 10 00 02 9d 36  8d ce b8 2f 00 00 00 00 00 00 00 00
                        logger.debug("Switching from known local port {} to this one {}", lastKnownPort,
                                rPacket.getPort());
                        this.lastKnownPort = rPacket.getPort();
                        sessionStateMachine(datagramSocket, StateMachineInput.SESSION_ESTABLISHED);
                        break;                                           
                    case (byte) 0xe0: // Device keep alive
                         echo1Packet = new DatagramPacket(rPacket.getData(), rPacket.getLength(), lastKnownIP,
                                lastKnownPort);
                        datagramSocket.send(echo1Packet); // echo back                       
                        sessionStateMachine(datagramSocket, StateMachineInput.KEEP_ALIVE_RECEIVED);
                        break;
                    case (byte) 0xe1: // device keep alive request
                         echo1Packet = new DatagramPacket(rPacket.getData(), rPacket.getLength(), lastKnownIP,
                                lastKnownPort);
                        datagramSocket.send(echo1Packet); // echo back
                        sessionStateMachine(datagramSocket, StateMachineInput.KEEP_ALIVE_RECEIVED);
                        break;            
                    // 80 00 00 00 15 (AC CF 23 F5 7A D4) 05 02 00 34 00 00 00 00 00 00 00 00 00 00 34
                    // Response to the registration packet
                    case (byte) 0xf0:  // device probe response
                        logUnknownPacket(buffer, len, "device in broken state");
                        sessionStateMachine(datagramSocket, StateMachineInput.INVALID_COMMAND);                            
                        break;
                    default:
                        logUnknownPacket(buffer, len, "No valid start byte");
                }
            }
        } catch (IOException e) {
            if (!willbeclosed) {
                logger.warn("Session Manager receive thread failed: {}", e.getLocalizedMessage(), e);
            }
        } finally {
            this.datagramSocket = null;
        }
            logger.debug("MilightCommunicationV6 receive thread stopped");
    }

    // Return true if the session is established successfully
    public boolean isValid() {
        return sessionState == SessionState.SESSION_VALID;
    }

    private void logStateMachine(StateMachineInput currentInput) {
        switch (sessionState) {
            case SESSION_INVALID:
                logger.debug("Current State SESSION_INVALID");                break;

            case SESSION_WAIT_FOR_BRIDGE:
                logger.debug("Current State WAIT FOR BRIDGE");                break;

            case SESSION_WAIT_FOR_SESSION_SID:
                logger.debug("Current State SESSION WAIT FOR SID");                break;

            case SESSION_NEED_REGISTER:
                logger.debug("Current State NEED REGISTER");                break;

            case SESSION_VALID:
                break;
             //   logger.debug("Current State session valid");                break;

            case SESSION_VALID_KEEP_ALIVE:
                logger.debug("current state keep alive");                break;

            default:
                logger.debug("UNKNOWN SESSION STATE");
        }
        switch (currentInput) {
            case NO_INPUT:
                logger.debug("<< NO INPUT");
                break;
            case TIMEOUT: logger.debug("<< TIMEOUT");                break;

            case INVALID_COMMAND: logger.debug("<< INVALID_COMMAND");                break;

            case KEEP_ALIVE_RECEIVED:
                break;
    //            logger.debug("<< KEEP ALIVE RECEIVED");                break;

            case BRIDGE_CONFIRMED:logger.debug("<< BRIDGE CONFIRMED");                break;

            case SESSION_ID_RECEIVED: logger.debug("<< SESSION ID RECEIVED");                break;

            case SESSION_ESTABLISHED:
                logger.debug("<< SESSION ESTABLISHED");                break;

            default:
                logger.debug("<<<< UNKNOWN");
        }

    }
}
