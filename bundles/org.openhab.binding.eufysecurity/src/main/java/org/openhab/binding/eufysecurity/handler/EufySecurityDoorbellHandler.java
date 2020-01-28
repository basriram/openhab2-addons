/**
 * /**
 *  * Copyright (c) 2010-2019 Contributors to the openHAB project
 *  *
 *  * See the NOTICE file(s) distributed with this work for additional
 *  * information.
 *  *
 *  * This program and the accompanying materials are made available under the
 *  * terms of the Eclipse Public License 2.0 which is available at
 *  * http://www.eclipse.org/legal/epl-2.0
 *  *
 *  * SPDX-License-Identifier: EPL-2.0
 *  *
  */
package org.openhab.binding.eufysecurity.handler;

import static org.openhab.binding.eufysecurity.EufySecurityBindingConstants.*;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.common.ThreadPoolManager;
import org.eclipse.smarthome.core.i18n.TimeZoneProvider;
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.RawType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.net.NetworkAddressService;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.UnDefType;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.eclipse.smarthome.io.transport.mqtt.MqttConnectionObserver;
import org.eclipse.smarthome.io.transport.mqtt.MqttConnectionState;
import org.eclipse.smarthome.io.transport.mqtt.MqttException;
import org.eclipse.smarthome.io.transport.mqtt.MqttMessageSubscriber;
import org.openhab.binding.eufysecurity.config.EufySecurityConfiguration;
import org.openhab.binding.eufysecurity.internal.ImageReadyListener;
import org.openhab.binding.eufysecurity.internal.api.EufySecurityMqttClient;
import org.openhab.binding.eufysecurity.internal.api.EufySecurityP2PClient;
import org.openhab.binding.eufysecurity.internal.api.EufySecurityP2PClient.ISessionState;
import org.openhab.binding.eufysecurity.internal.api.EufySecurityP2PClient.SessionState;
import org.openhab.binding.eufysecurity.internal.api.model.CameraEvent;
import org.openhab.binding.eufysecurity.internal.api.model.EufySecuritySystem;
import org.openhab.binding.eufysecurity.internal.api.model.Station;
import org.openhab.binding.eufysecurity.internal.api.model.generated.DeviceSettingMessage;
import org.eclipse.smarthome.core.thing.CommonTriggerEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link EufySecurityDoorbellHandler} handles the actions for a Eufy Doorbell.
 * Only the Doorbell device has MQTT based notification mechanism
 *
 * @author Sriram Balakrishnan - Initial contribution
 */
@NonNullByDefault
public class EufySecurityDoorbellHandler extends EufySecurityDeviceHandler
        implements MqttMessageSubscriber, MqttConnectionObserver, ImageReadyListener, ISessionState {
    private final Logger logger = LoggerFactory.getLogger(EufySecurityDoorbellHandler.class);

    private final String deviceSN;
    private @Nullable EufySecurityMqttClient mqttClient;
    private String clientId;
    private @Nullable EufySecurityP2PClient p2pClient;
    private @Nullable InetAddress p2pAddress;
    private int p2pPort;
    private @Nullable DatagramSocket p2pSocket;
    private String offlineReason = "";
    private @Nullable ScheduledFuture<?> scheduledFuture;
    private @NonNullByDefault({}) EufySecurityConfiguration config;

    private final TimeZoneProvider timeZoneProvider;

    // Get a dedicated threadpool for the long-running listener thread
    private final ScheduledExecutorService doorbellScheduler = ThreadPoolManager
            .getScheduledPool("eufysecuritydoorbellHandler");
    private @Nullable ScheduledFuture<?> listenerJob;

    private @Nullable ScheduledFuture<?> imageRefreshJob;
    private @Nullable ScheduledFuture<?> doorbellOffJob;
    private @Nullable ScheduledFuture<?> motionOffJob;


    public EufySecurityDoorbellHandler(Thing thing, EufySecuritySystem system,
            NetworkAddressService networkAddressService, TimeZoneProvider timeZoneProvider) {
        super(thing, system);
        this.timeZoneProvider = timeZoneProvider;
        this.config = getConfigAs(EufySecurityConfiguration.class);
        deviceSN = thing.getConfiguration().get(DEVICE_SN).toString();
        clientId = EufySecurityMqttClient.getClientId((@NonNull String) eufySecuritySystem.getUserId(),
                (@NonNull String) networkAddressService.getPrimaryIpv4HostAddress().toString());
        logger.debug("Client ID is {}", clientId);
    }

    @Override
    public void initialize() {
        // Because initialization can take longer a scheduler with an extra thread is
        // created
        scheduler.schedule(() -> {
            if (createMqttConnection()) {
                updateStatus(ThingStatus.ONLINE);
            } else {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "Unable to obtain MQTT connection");
            }
        }, 3, TimeUnit.SECONDS);
    }

    @Override
    public void dispose() {
        if (p2pClient != null) {
            try {
                p2pClient.close();
            } catch (IOException ignore) {

            }
            this.p2pClient = null;
        }
        mqttClient.stopProfileConnection();
        stopDoorbellOffJob();
        stopMotionOffJob();
    }

    private boolean crateP2PConnection() {
        boolean success = false;
        try {
            Map<String, Station> staionsList = eufySecuritySystem.getAllStations();
            Station myStation = staionsList.values().iterator().next(); // get the first station from the list
            p2pClient = new EufySecurityP2PClient(myStation, this);
            p2pClient.start().thenAccept(socket -> this.p2pSocket = socket);
            success = true;
        } catch (Exception ce) {
            ce.printStackTrace();
            logger.error("error creating p2p connection", ce.getMessage());
            success = false;
        }
        return success;
    }

    private boolean createMqttConnection() {
        boolean success = false;
        try {
            mqttClient = new EufySecurityMqttClient(clientId, this, this);
            mqttClient.startProfileConnection((@NonNull String) eufySecuritySystem.getRegion(),
                    (@NonNull String) this.deviceSN);
            success = true;
        } catch (CertificateException ce) {
            logger.error("Ceriticate error cerating mqtt connection {}", ce.getMessage());
            success = false;
        } catch (MqttException me) {
            success = false;
        }
        return success;
    }

    // Callback used by listener to update doorbell channel
    public void updateDoorbellChannel(CameraEvent doorbellEvent) {
        logger.debug("Handler: Update DOORBELL channels for thing {}", getThing().getUID());
       if (doorbellEvent.isRawImageAvailable() && doorbellEvent.getImageContentType()!=null) {
            RawType image = new RawType((byte @NonNull[])doorbellEvent.getRawImage(), (@NonNull String) doorbellEvent.getImageContentType());
            updateState(CHANNEL_DOORBELL_IMAGE, image != null ? image : UnDefType.UNDEF);
        } 
        final @NonNull String picUrl = (@NonNull String) doorbellEvent.getPicUrls().get(0);
        updateState(CHANNEL_DOORBELL_TIMESTAMP, getLocalDateTimeType(doorbellEvent.getCreatedTime()));
        triggerChannel(CHANNEL_DOORBELL, CommonTriggerEvents.PRESSED);
        updateState(CHANNEL_IMAGE_URL, new StringType(picUrl));
        startDoorbellOffJob();
    }

    // Callback used by listener to update motion channel
    public void updateMotionChannel(CameraEvent motionEvent) {
        logger.debug("Handler: Update MOTION channels for thing {}", getThing().getUID());
       if (motionEvent.isRawImageAvailable() && motionEvent.getImageContentType()!=null) {
            RawType image = new RawType((byte @NonNull[])motionEvent.getRawImage(), (@NonNull String) motionEvent.getImageContentType());
            updateState(CHANNEL_MOTION_IMAGE, image != null ? image : UnDefType.UNDEF);
        } 
        updateState(CHANNEL_MOTION_TIMESTAMP, getLocalDateTimeType(motionEvent.getCreatedTime()));
        updateState(CHANNEL_MOTION, OnOffType.ON);
        updateState(CHANNEL_IMAGE_URL, new StringType(motionEvent.getPicUrls().get(0)));
        startMotionOffJob();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Got command {} for channel {} of thing {}", command, channelUID, getThing().getUID());

        switch (channelUID.getId()) {
            case CHANNEL_DOORBELL_IMAGE:
                if (command instanceof RefreshType) {
                    refreshDoorbellImageFromHistory();
                }
                break;
            case CHANNEL_MOTION_IMAGE:
                if (command instanceof RefreshType) {
                    refreshMotionImageFromHistory();
                }
                break;
            case CHANNEL_IMAGE:
                if (command instanceof RefreshType) {
                    handleGetImage(command);
                }
                break;
            case CHANNEL_CHIME_MODE:
                this.handleChimeMode(command);
                break;
        }
    }

    private void handleChimeMode(Command command) {
        //     p2pClient.makeCommand(wb, zone, data);
        //     p2pClient.queueCommand();
    }

    private void refreshDoorbellImageFromHistory() {
        logger.warn("TODO: NOT IMPELMENTED YET");
        updateState(CHANNEL_DOORBELL, OnOffType.OFF);
    }

    private void refreshMotionImageFromHistory() {
        logger.warn("TODO: NOT IMPELMENTED YET");
        updateState(CHANNEL_MOTION, OnOffType.OFF);

    }

    private void handleGetImage(Command command) {
        if (command instanceof OnOffType && command.equals(OnOffType.ON)) {
            logger.warn("TODO: NOT IMPELMENTED YET");
        }
    }

    private void startDoorbellOffJob() {
        Integer offDelay = config.doorbellOffDelay;
        if (offDelay == null) {
            return;
        }
        if (doorbellOffJob != null) {
            doorbellOffJob.cancel(true);
        }
        doorbellOffJob = doorbellScheduler.schedule(() -> {
            logger.debug("Update channel 'doorbell' to OFF for thing {}", getThing().getUID());
            triggerChannel(CHANNEL_DOORBELL, CommonTriggerEvents.RELEASED);
        }, offDelay, TimeUnit.SECONDS);
    }

    private void stopDoorbellOffJob() {
        if (doorbellOffJob != null) {
            doorbellOffJob.cancel(true);
            doorbellOffJob = null;
            logger.debug("Canceling doorbell off job");
        }
    }

    private void startMotionOffJob() {
        Integer offDelay = config.motionOffDelay;
        if (offDelay == null) {
            return;
        }
        if (motionOffJob != null) {
            motionOffJob.cancel(true);
        }
        motionOffJob = doorbellScheduler.schedule(() -> {
            logger.debug("Update channel 'motion' to OFF for thing {}", getThing().getUID());
            updateState(CHANNEL_MOTION, OnOffType.OFF);
        }, offDelay, TimeUnit.SECONDS);
    }

    private void stopMotionOffJob() {
        if (motionOffJob != null) {
            motionOffJob.cancel(true);
            motionOffJob = null;
            logger.debug("Canceling motion off job");
        }
    }

    private DateTimeType getLocalDateTimeType(long dateTimeMilliSeconds) {
        logger.debug("Long timestamp is {}", dateTimeMilliSeconds);
        DateTimeType result = new DateTimeType(
                Instant.ofEpochSecond(dateTimeMilliSeconds / 1000).atZone(timeZoneProvider.getTimeZone()));
        logger.debug("Converted timestamp is {}", result);
        return result;

    }

    @Override
    public void setStatusOffline() {
        mqttClient.stopProfileConnection();
        updateStatus(ThingStatus.OFFLINE);
    }

    @Override
    public void setStatusOnline() {
        this.initialize();
    }

    protected void handleDeviceSettingMessage(DeviceSettingMessage deviceSettingMessage) {
        CameraEvent cameraEvent = eufySecuritySystem.parseCameraEvent(deviceSettingMessage, this);
        if (cameraEvent != null) {
            if (cameraEvent.isDoorbellEvent()) {
                updateDoorbellChannel(cameraEvent);
            } else {
                updateMotionChannel(cameraEvent);
            }
        } else {
            logger.warn("Unable to parse camera event. no notifications done");
        }

    }

    @Override
    public void connectionStateChanged(MqttConnectionState state, @Nullable Throwable error) {
        logger.warn("TODO: Lost MQTT connectivity, need to handle re-connect");
    }

    @Override
    public void processMessage(String topic, byte[] payload) {
        logger.debug("got MQTT message for topic {}", topic);
        try {
            DeviceSettingMessage message = DeviceSettingMessage.parseFrom(payload);
            handleDeviceSettingMessage(message);
            logger.debug("Successfully parsed {}", message);
        } catch (InvalidProtocolBufferException e) {
            logger.error("Unable to parse device settings message {}", String.valueOf(payload));
        }
    }

@Override
    public void imageReady(CameraEvent event) {
        if (event.isRawImageAvailable() && event.getRawImage()!=null && event.getImageContentType()!=null){
            RawType image = new RawType((byte @NonNull[]) event.getRawImage(), (@NonNull String)event.getImageContentType());
            if (event.isDoorbellEvent())
                updateState(CHANNEL_DOORBELL_IMAGE, image != null ? image : UnDefType.UNDEF);
            else
                updateState(CHANNEL_MOTION_IMAGE, image != null ? image : UnDefType.UNDEF);
        }
    }

    @Override
    public void sessionStateChanged(SessionState state, @Nullable InetAddress address, int port) {
        switch (state) {
            case SESSION_VALID_KEEP_ALIVE:
                Instant lastSessionTime = p2pClient.getLastSessionValidConfirmation();
                LocalDateTime date = LocalDateTime.ofInstant(lastSessionTime, ZoneId.systemDefault());
               // updateProperty(MilightBindingConstants.PROPERTY_SESSIONCONFIRMED, date.format(timeFormat));
                break;
            case SESSION_VALID:
                if (address == null) {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "No IP address received");
                    break;
                }
                if (!address.equals(p2pAddress) || p2pPort != port || !thing.getStatus().equals(ThingStatus.ONLINE)) {
                    updateStatus(ThingStatus.ONLINE);
                    this.p2pAddress = address;
                    this.p2pPort = port;
                }
                break;
            case SESSION_INVALID:
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "Session could not be established");

                break;
            default:
                // Delay putting the session offline
                offlineReason = state.name();
                logger.warn("State machine issue {}", offlineReason);
                updateStatus(ThingStatus.ONLINE);
              //  scheduledFuture = scheduler.schedule(
              //          () -> updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_PENDING, offlineReason),
              //          1000, TimeUnit.MILLISECONDS);
                break;
        }
    }
}
