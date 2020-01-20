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

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Hub Connection Data Object. Field names obtained from REST Service JSON structure
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
public class HubConnData {
    public String app_conn;
    public boolean binded;
    public String main_hw_version;
    public String main_sw_version;
    public String ndt_did;
    public String p2p_conn;
    public String p2p_did;
    public String push_did;
    public String station_model;
    public String station_name;
    public String station_sn;
}