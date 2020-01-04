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
 *  */
 */
package org.openhab.binding.eufysecurity.internal.api;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link EufySecurityBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Sriram Balakrishnan - Initial contribution
 */
@NonNullByDefault
public class EufySecurityConstants {

    // URL for REST service end point
    public static final String PRIMARY_URL = "https://mysecurity.eufylife.com/api/v1";


    // MQTT is used by Doorbell to receive notification for things such as motion detected/doorbell rang etc.,
    public static final String MQTT_CI_PASSWORD = "roav#2018";
    public static final String MQTT_CI_USER_NAME = "eufy_security_client";
    public static final String MQTT_EU_PASSWORD = "eufyEU_#$2019";
    public static final String MQTT_EU_USER_NAME = "eufy_security_client";
    public static final String MQTT_HOST_CI = "ssl://security-mqtt-ci.eufylife.com";
    public static final String MQTT_HOST_EU = "ssl://security-mqtt-eu.eufylife.com";
    public static final String MQTT_HOST_QA = "ssl://security-mqtt-qa.eufylife.com";
    public static final String MQTT_HOST_SQA = "ssl://security-mqtt-short-qa.eufylife.com";
    public static final String MQTT_HOST_US = "security-mqtt.eufylife.com";
    public static final String MQTT_QA_PASSWORD = "roav#2018";
    public static final String MQTT_QA_USER_NAME = "eufy_security_client";
    public static final String MQTT_SQA_PASSWORD = "roav#2018";
    public static final String MQTT_SQA_USER_NAME = "eufy_security_client";
    public static final String MQTT_US_PASSWORD = "eufyUS_!@2019";
    public static final String MQTT_US_USER_NAME = "eufy_security_client";

    // Topic from which the device receives notification
    public static String deviceTopic = "/phone/doorbell/%s/push_message";

    // Seconds to wait before tryingt download image after event has occured
    public static final Integer IMAGE_DOWNLOAD_DELAY = 10; 

}
