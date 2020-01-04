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
package org.openhab.binding.eufysecurity;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@link EufySecurityBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Sriram Balakrishnan - Initial contribution
 */
@NonNullByDefault
public class EufySecurityBindingConstants {

    private static final String BINDING_ID = "eufysecurity";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_BRIDGE = new ThingTypeUID(BINDING_ID, "bridge");

    // Only device supported for now is the doorbell
    public static final ThingTypeUID THING_TYPE_T8200 = new ThingTypeUID(BINDING_ID, "T8200");

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Stream.of(THING_TYPE_BRIDGE, THING_TYPE_T8200)
            .collect(Collectors.toSet());

    // List of all Channel IDs
    public static final String CHANNEL_DOORBELL = "doorbell";
    public static final String CHANNEL_DOORBELL_TIMESTAMP = "doorbellTimestamp";
    public static final String CHANNEL_DOORBELL_IMAGE = "doorbellImage";
    public static final String CHANNEL_MOTION = "motion";
    public static final String CHANNEL_MOTION_TIMESTAMP = "motionTimestamp";
    public static final String CHANNEL_MOTION_IMAGE = "motionImage";
    public static final String CHANNEL_IMAGE = "image";
    public static final String CHANNEL_IMAGE_TIMESTAMP = "imageTimestamp";
    public static final String CHANNEL_DOORBELL_HISTORY_INDEX = "doorbellHistoryIndex";
    public static final String CHANNEL_DOORBELL_HISTORY_IMAGE = "doorbellHistoryImage";
    public static final String CHANNEL_DOORBELL_HISTORY_TIMESTAMP = "doorbellHistoryTimestamp";
    public static final String CHANNEL_MOTION_HISTORY_INDEX = "motionHistoryIndex";
    public static final String CHANNEL_MOTION_HISTORY_IMAGE = "motionHistoryImage";
    public static final String CHANNEL_MOTION_HISTORY_TIMESTAMP = "motionHistoryTimestamp";

    public static final String DEVICE_NAME = "Name";
    public static final String DEVICE_SN = "DeviceSN";
    public static final String DEVICE_MODEL = "Model";
    public static final String DEVICE_IP = "IPAddress";
    public static final String DEVICE_SW_VERSION = "Version";
}
