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
 * Member Object. Field names obtained from REST Service JSON structure
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
public class Member {
    public static final int TYPE_ADMIN = 1;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SUPER_ADMIN = 2;
    public String action_user_id;
    public String action_user_name;
    public String admin_user_id;
    public String avatar;
    public int create_time;
    public String email;
    public int family_id;
    public int fence_state;
    public String member_nick;
    public int member_type;
    public String member_user_id;
    public String nick_name;
    public String station_sn;
    public int status;
}