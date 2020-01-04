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
package org.openhab.binding.eufysecurity.internal;

import static org.openhab.binding.eufysecurity.EufySecurityBindingConstants.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.i18n.TimeZoneProvider;
import org.eclipse.smarthome.core.net.NetworkAddressService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Component;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.smarthome.io.net.http.HttpClientFactory;
import org.openhab.binding.eufysecurity.discovery.EufySecurityDeviceDiscovery;
import org.openhab.binding.eufysecurity.handler.EufySecurityBridgeHandler;
import org.openhab.binding.eufysecurity.handler.EufySecurityDoorbellHandler;
import org.openhab.binding.eufysecurity.internal.api.model.EufySecuritySystem;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.framework.ServiceRegistration;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link EufySecurityHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Sriram Balakrishnan - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.eufysecurity", service = ThingHandlerFactory.class)
public class EufySecurityHandlerFactory extends BaseThingHandlerFactory {
    private final HttpClient httpClient;
    private final Logger logger = LoggerFactory.getLogger(EufySecurityHandlerFactory.class);
    private EufySecuritySystem eufySecuritySystem;
    private final TimeZoneProvider timeZoneProvider;

    private @NonNullByDefault({}) NetworkAddressService networkAddressService;
    private Map<ThingUID, ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();

    @Activate
    public EufySecurityHandlerFactory(@Reference HttpClientFactory httpClientFactory,
            @Reference TimeZoneProvider timeZoneProvider) {
        this.httpClient = httpClientFactory.getCommonHttpClient();
        this.timeZoneProvider = timeZoneProvider;
        eufySecuritySystem = new EufySecuritySystem(httpClient);
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_BRIDGE.equals(thingTypeUID)) {
            EufySecurityBridgeHandler bridgeHandler = new EufySecurityBridgeHandler((Bridge) thing, eufySecuritySystem);
            EufySecurityDeviceDiscovery deviceDiscovery = new EufySecurityDeviceDiscovery(bridgeHandler);
            discoveryServiceRegs.put(bridgeHandler.getThing().getUID(), bundleContext
                    .registerService(DiscoveryService.class.getName(), deviceDiscovery, new Hashtable<>()));
            logger.debug("Register discovery service for Eufy Security devices and stations by bridge '{}'",
                    bridgeHandler.getThing().getUID().getId());
            return bridgeHandler;
        }
        if (THING_TYPE_T8200.equals(thingTypeUID)) {
            EufySecurityDoorbellHandler doorbellHandler = new EufySecurityDoorbellHandler(thing, eufySecuritySystem,
                    networkAddressService, timeZoneProvider);
            return doorbellHandler;
        }
        return null;
    }

    @Reference
    protected void setNetworkAddressService(NetworkAddressService networkAddressService) {
        this.networkAddressService = networkAddressService;
    }

    protected void unsetNetworkAddressService(NetworkAddressService networkAddressService) {
        this.networkAddressService = null;
    }

}
