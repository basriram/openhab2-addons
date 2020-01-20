package org.openhab.binding.eufysecurity.internal.api.model;

import org.eclipse.jdt.annotation.Nullable;

public class P2PMessage {

    final private int[] p2pMessageSignature = { 0x58, 0x5a, 0x59, 0x48 };
    final private int p2pFirstByte = 0xf1;
    final private int p2pStunRequest = 0x00;
    final private int p2pUIDLookupRequest = 0x20;
    final private int p2pUIDLookupResponse = 0x40;
    final private int p2pUIDLookupAckResponse = 0x21;


    private int sessionId = -1;

    @Nullable private String deviceDID;

    // STUN Hello f1 00 00 00
    // STUN UID Request 
    //   f1 20 00 24 53 45 43 43  41 4d 41 00 00 01 59 70   . .$SECC AMA...Yp
    //   59 53 43 58 46 00 00 00  00 02 42 45 5e 1e a8 c0   YSCXF... ..BE^...
    //   00 00 00 00 00 00 00 00 
    // STUN UID Lookup Response
    // f1 40 00 10 00 02 9d 36  8d ce b8 2f 00 00 00 00 00 00 00 00
    // UID Lookup ACK
    // f1 21 00 04 00 00 00 00
    // device probe request to device ip
    // f1 41 00 14 53 45 43 43 41 4d 41 00 00 01 59 70 59 53 43 58 46 00 00 00
    // device probe response
    // f1 41 00 14 53 45 43 43 41 4d 41 00 00 01 59 70 59 53 43 58 46 00 00 00

    // some form of heart beat from us to device
    // f1 e0 00 00
    // device responds with 
    // f1 e0 00 00
    // device sends heart beat to us
    // f1 e1 00 00
    // our response
    // f1 e1 00 00

    // f1 d0 00 14 d1 00 00 00 58 5a 59 48 73 04 00 00 00 00 01 00 ff 00 00 00 // from client to device
    // f1 d1 00 06 d1 00 00 01 00 00 // device to client response
 
    // f1 d0 00 1c d1 02 00 00 58 5a 59 48 73 04 08 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 // from device to client
    // f1 d1 00 0a d1 02 00 03 00 00 00 00 00 00 // from client to device response
}
