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
package org.openhab.binding.eufysecurity.internal.api.model;

import java.util.List;
/**
 * Station Object. Field names obtained from REST Service JSON structure
 * Eufy code seems to refer to this as Station and Hub
 * 
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
public class Station {

    public int ai_algor_time;
    public String ai_algor_version;
    public int ai_kernel_time;
    public String ai_kernel_version;
    public int ai_rootfs_time;
    public String ai_rootfs_version;
    public String app_conn;
    public String bt_mac;
    public int create_time;
    public List<DeviceInfo> devices;
    public int event_num = 0;
    public String ip_addr;
    public boolean isOnline = false;
    public String main_hw_version;
    public int main_sw_time;
    public String main_sw_version;
    public Member member;
    public String ndt_did;
    public String ndt_license;
    public String p2p_conn;
    public String p2p_did;
    public String p2p_license;
    public List<StationParam> params;
    public String push_did;
    public String push_license;
    public String query_server_did;
    public String sec_hw_version;
    public int sec_sw_time;
    public String sec_sw_version;
    public int station_id;
    public String station_model;
    public String station_name;
    public String station_sn;
    public int status;
    public String sub1g_mac;
    public long time_stamp = 0;
    public String time_zone;
    public String volume;
    public String wifi_mac;
    public String wifi_ssid;
    public String wipn_enc_dec_key;
    public String wipn_ndt_aes128key;

}