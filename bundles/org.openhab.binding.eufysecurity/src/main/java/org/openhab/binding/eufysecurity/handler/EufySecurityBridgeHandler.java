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
package org.openhab.binding.eufysecurity.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openhab.binding.eufysecurity.internal.api.NotAuthorizedException;

import org.openhab.binding.eufysecurity.internal.api.model.*;
import org.openhab.binding.eufysecurity.config.EufySecurityConfiguration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;

/**
 * The {@link EufySecurityHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Sriram Balakrishnan - Initial contribution
 */
@NonNullByDefault
public class EufySecurityBridgeHandler extends BaseBridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(EufySecurityBridgeHandler.class);

    private @Nullable EufySecurityConfiguration config;

    private EufySecuritySystem eufySecuritySystem;

    public EufySecurityBridgeHandler(Bridge thing, EufySecuritySystem eufySecuritySystem) {
        super(thing);
        this.eufySecuritySystem = eufySecuritySystem;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // do nothing
    }

    @Override
    public void initialize() {
        scheduler.schedule(this::configure, 0, TimeUnit.SECONDS);
    }

    /**
     * Configures this thing
     */
    private void configure() {
        EufySecurityConfiguration configuration = getConfig().as(EufySecurityConfiguration.class);
        String emailAddress = configuration.emailAddress;
        String password = configuration.userPassword;
        String region = configuration.region;
        try {
            boolean isConnected = eufySecuritySystem.establishConnection(emailAddress, password);
            if (isConnected) {
                logger.debug("Eufy Security System connected successfully");
                eufySecuritySystem.setRegion(region);
                updateStatus(ThingStatus.ONLINE);
            } else {
                logger.warn("Unable to connect to Eufy Security Servers");
                updateStatus(ThingStatus.UNINITIALIZED);
            }
        } catch (IOException e) {
            logger.debug("Could not connect to service {}", e.getMessage());
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        } catch (NotAuthorizedException e) {
            logger.debug("Credentials not valid");
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Credentials not valid");
        }
    }

    public EufySecuritySystem getEufySecuritySystem() {
        return this.eufySecuritySystem;
    }

}
