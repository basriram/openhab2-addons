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
package org.openhab.binding.eufysecurity.handler;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingStatusInfo;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.builder.ThingBuilder;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.eufysecurity.internal.api.model.EufySecuritySystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link EufySecurityDeviceHandler} class is the base Class all Eufy Security Devices.
 * It provides basic command handling and common needed methods.
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
@NonNullByDefault
public abstract class EufySecurityDeviceHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(EufySecurityDeviceHandler.class);

    protected String id;

    protected EufySecurityBridgeHandler bridge;
    protected EufySecuritySystem eufySecuritySystem;

    public EufySecurityDeviceHandler(Thing thing, EufySecuritySystem eufySecuritySystem) {
        super(thing);
        this.eufySecuritySystem = eufySecuritySystem;
    }

    public abstract void setStatusOffline();

    public abstract void setStatusOnline();

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.warn("TODO: NOT IMPLEMENTED YET {}", command);
    }

    @Override
    public void bridgeStatusChanged(ThingStatusInfo bridgeStatusInfo) {
        if (ThingStatus.OFFLINE.equals(bridgeStatusInfo.getStatus())) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
        } else if (ThingStatus.ONLINE.equals(bridgeStatusInfo.getStatus())) {
            updateStatus(ThingStatus.ONLINE);
        } else if (ThingStatus.UNINITIALIZED.equals(bridgeStatusInfo.getStatus())) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_UNINITIALIZED);
        }
    }

    protected void updateThingChannels(List<Channel> channelList) {
        ThingBuilder thingBuilder = editThing();
        thingBuilder.withChannels(channelList);
        updateThing(thingBuilder.build());
    }

    /**
     * Dispose the handler and unregister the handler
     * form Change Events
     */
    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * Handles the updates send from the Eufy system to
     * the binding. 
     * 
     * @param event
     * @param command
     */
    @SuppressWarnings("null")
    protected void handleThingStateUpdate(String event, String command) {

    }

}
