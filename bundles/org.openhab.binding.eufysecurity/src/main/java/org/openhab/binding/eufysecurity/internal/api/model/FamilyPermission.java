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
package org.openhab.binding.eufysecurity.internal.api.model;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * FamilyPermission Object. Field names obtained from REST Service JSON structure
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
@NonNullByDefault
public class FamilyPermission {
    public String action_user_id;
    public int create_time;
    public String device_sn;
    public String member_user_id;
    public int permission_id;
    public int permissions;
    public int status;
}