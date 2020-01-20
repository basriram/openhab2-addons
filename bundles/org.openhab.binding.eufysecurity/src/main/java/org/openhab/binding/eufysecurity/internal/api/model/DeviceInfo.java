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


/**
 * Device Info Object. Field names obtained from REST Service JSON structure
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
public class DeviceInfo {
    public static final int TYPE_DEVICE_CAMERA = 1;
    public static final int TYPE_DEVICE_FLOODLIGHT = 3;
    public static final int TYPE_DEVICE_SENSOR = 2;
    public int create_time;
    public int device_channel;
    public int device_id;
    public String device_model;
    public String device_name;
    public String device_sn;
    public int device_type;
    public int event_num;
    public String main_hw_version;
    public String main_sw_version;
    public String schedule;
    public String sec_hw_version;
    public String sec_sw_version;
    public String station_sn;
    public int status;
    public String sub1g_mac;
    public String wifi_mac;
}