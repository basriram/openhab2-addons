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
package org.openhab.binding.eufysecurity.config;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link EufySecurityConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Sriram Balakrishnan - Initial contribution
 */
@NonNullByDefault
public class EufySecurityConfiguration {

    /**
     * email Address of registered user
     */
    public String emailAddress;

    /**
     * Password of registered user
     */
    public String userPassword;

    /**
     * Delay before refreshing image
     */
    public Integer imageRefreshRate;

    /**
     * Delay to set doorbell channel OFF after doorbell event
     */
    public Integer doorbellOffDelay;

    /**
     * Delay to set motion channel OFF after motion event
     */
    public Integer motionOffDelay;

    /**
     * region where this device is registered (US or EU)
     */    
    public String region;
}
