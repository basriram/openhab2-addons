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
 * Device Object. Field names obtained from REST Service JSON structure
 * 
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
public class Device {
    public static final int TYPE_CAMERA_GUN = 101;
    public static final int TYPE_CAMERA_SNAIL = 102;
    public static final int TYPE_DEVICE_CAMERA = 1;
    public static final int TYPE_DEVICE_CAMERA2 = 9;
    public static final int TYPE_DEVICE_CAMERA2C = 8;
    public static final int TYPE_DEVICE_CAMERA_E = 4;
    public static final int TYPE_DEVICE_DOORBELL = 5;
    public static final int TYPE_DEVICE_FLOODLIGHT = 3;
    public static final int TYPE_DEVICE_SENSOR = 2;
    public static final int TYPE_DEVICE_STATION = 0;
    public int bind_time;
    public String bt_mac;
    public String cover_path;
    public long cover_time;
    public int create_time;
    public int device_channel;
    public int device_id;
    public String device_model;
    public String device_name;
    public String device_sn;
    public int device_type;
    public int event_num;
    public int family_num;
    public String ip_addr;
    public String local_ip;
    public String main_hw_version;
    public int main_sw_time;
    public String main_sw_version;
    public Member member;
    public int month_pir_none;
    public int month_pir_total;
    public List<DeviceParam> params;
    public FamilyPermission permission;
    public int pir_none;
    public int pir_total;
    public String schedule;
    public String schedulex;
    public String sec_hw_version;
    public int sec_sw_time;
    public String sec_sw_version;
    public int sector_id;
    public String sector_name;
    public HubConnData station_conn;
    public String station_sn;
    public int status;
    public String sub1g_mac;
    public String svr_domain;
    public int svr_port;
    public String time_zone;
    public String wifi_mac;
    public String wifi_ssid;

    public boolean isIntegratedDevice() {
        return isFloodLight() || isDoorbell();
    }

    public boolean isFloodLight() {
        return 3 == this.device_type;
    }

    public boolean isDoorbell() {
        return 5 == this.device_type;
    }

    public boolean isCamera() {
        int i = this.device_type;
        return i == 1 || i == 4 || i == 101 || i == 102;
    }

    public boolean isCamera1Gun() {
        return 101 == this.device_type;
    }

    public boolean isCamera1Snail() {
        return 102 == this.device_type;
    }

    public boolean isCamera2() {
        return 9 == this.device_type;
    }

    public boolean isCamera2C() {
        return 8 == this.device_type;
    }

    public String getSerialNumber() {
        return this.device_sn;
    }
}