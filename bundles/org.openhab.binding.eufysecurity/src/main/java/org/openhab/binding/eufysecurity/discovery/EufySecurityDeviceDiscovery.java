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
package org.openhab.binding.eufysecurity.discovery;

import static org.openhab.binding.eufysecurity.EufySecurityBindingConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.eufysecurity.handler.EufySecurityBridgeHandler;
import org.openhab.binding.eufysecurity.internal.api.model.Device;
import org.openhab.binding.eufysecurity.internal.api.model.EufySecuritySystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link EufySecurityDeviceDiscovery} discovers the devices and hubs within
 * the Eufy Security Devices network.
 *
 * @author Sriram Balakrishnan - Initial contribution
 */
@NonNullByDefault
public class EufySecurityDeviceDiscovery extends AbstractDiscoveryService {
    private final Logger logger = LoggerFactory.getLogger(EufySecurityDeviceDiscovery.class);

    private static final int SEARCH_TIME = 5;
    private static final int INITIAL_DELAY = 5;
    private static final int SCAN_INTERVAL = 20;

    private final EufySecurityBridgeHandler bridge;

    private ScheduledFuture<?> scanningJob;

    public EufySecurityDeviceDiscovery(final EufySecurityBridgeHandler bridge) throws IllegalArgumentException {
        super(SEARCH_TIME);
        this.bridge = bridge;
    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypes() {
        return Stream.of(THING_TYPE_T8200).collect(Collectors.toSet());
    }

    @Override
    protected void startScan() {
        final EufySecuritySystem system = bridge.getEufySecuritySystem();
        if (!system.isConnected()) {
            logger.debug("Scan for devices not possible.  Bridge is not connected");
            return;
        }
        logger.debug("Start scan for eufy security devices");

        Map<String, Device> deviceMap = new HashMap<>();
        deviceMap = system.getAllDevices();

        if (deviceMap == null) {
            return;
        } else {
            logger.debug("Found: {} new Device", deviceMap.size());
            final ThingUID bridgeUID = bridge.getThing().getUID();

            for (final String deviceSN : deviceMap.keySet()) {
                final Device device = deviceMap.get(deviceSN);
                // Only handling doorbell devices for now
                if (device.isDoorbell()) {
                    final ThingUID uid = new ThingUID(THING_TYPE_T8200, deviceSN);
                    final Map<String, Object> properties = new HashMap<>();
                    properties.put(DEVICE_MODEL, device.device_model);
                    properties.put(DEVICE_NAME, device.device_name);
                    properties.put(DEVICE_SN, device.device_sn);
                    properties.put(DEVICE_IP, device.local_ip);
                    properties.put(DEVICE_SW_VERSION, device.main_sw_version);
                    final DiscoveryResult result = DiscoveryResultBuilder.create(uid).withLabel(device.device_sn)
                            .withProperties(properties).withBridge(bridgeUID).build();
                    thingDiscovered(result);
                } else {
                    logger.warn("Unsupported device found. ignoring {}", device.getSerialNumber());
                }
            }
        }
    }

    @Override
    protected void startBackgroundDiscovery() {
        logger.trace("Starting background discovery");
        if (scanningJob == null || scanningJob.isCancelled()) {
            this.scanningJob = scheduler.scheduleWithFixedDelay(this::startScan, INITIAL_DELAY, SCAN_INTERVAL,
                    TimeUnit.SECONDS);
        }
        logger.trace("scanningJob active");
    }

    @Override
    protected void stopBackgroundDiscovery() {
        logger.debug("Stop background discovery");
        if (scanningJob != null && !scanningJob.isCancelled()) {
            scanningJob.cancel(true);
            scanningJob = null;
        }
    }

    @Override
    protected synchronized void stopScan() {
        super.stopScan();
    }

}
