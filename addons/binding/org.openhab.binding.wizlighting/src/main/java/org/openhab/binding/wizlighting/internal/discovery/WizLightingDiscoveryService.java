/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wizlighting.internal.discovery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.AuthenticationStore;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.smarthome.config.core.ConfigConstants;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.wizlighting.WizLightingBindingConstants;
import org.openhab.binding.wizlighting.handler.WizLightingMediator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This is the {@link DiscoveryService} for the Wizlighting Items.
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
public class WizLightingDiscoveryService extends AbstractDiscoveryService {

    private final Logger logger = LoggerFactory.getLogger(WizLightingDiscoveryService.class);
    private WizLightingMediator mediator;
    private String inviteToken;
    private Gson gson;
    private HttpClient httpClient;

    @Override
    protected void activate(Map<String, Object> configProperties) {
        if (configProperties != null) {
            Object property = configProperties.get(WizLightingBindingConstants.DISCOVERY_INVITE_TOKEN);
            if (property != null) {
                this.inviteToken = String.valueOf(property);
            }
        }
    }

    /**
     * Used by OSGI to inject the mediator in the discovery service.
     *
     * @param mediator the mediator
     */
    public void setMediator(final WizLightingMediator mediator) {
        logger.debug("Mediator has been injected on discovery service.");
        this.mediator = mediator;
        mediator.setDiscoveryService(this);
    }

    /**
     * Used by OSGI to unset the mediator in the discovery service.
     *
     * @param mediator the mediator
     */
    public void unsetMediator(final WizLightingMediator mitsubishiMediator) {
        logger.debug("Mediator has been unsetted from discovery service.");
        this.mediator.setDiscoveryService(null);
        this.mediator = null;
    }

    /**
     * Constructor of the discovery service.
     *
     * @throws IllegalArgumentException if the timeout < 0
     */
    public WizLightingDiscoveryService() throws IllegalArgumentException {
        super(WizLightingBindingConstants.SUPPORTED_THING_TYPES_UIDS,
                WizLightingBindingConstants.DISCOVERY_TIMEOUT_SECONDS);
    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypes() {
        return WizLightingBindingConstants.SUPPORTED_THING_TYPES_UIDS;
    }

    @Override
    protected void startScan() {
        if (inviteToken == null) {
            String folderName = ConfigConstants.getUserDataFolder() + "/";
            File file = new File(folderName, WizLightingBindingConstants.INVITE_TOKEN_TEMP_FILE);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                    String line = reader.readLine();
                    reader.close();
                    if (line != null && line.length() > 0) {
                        this.inviteToken = line;
                    }
                } catch (IOException e) {
                    logger.error("Error reading invite token file {}", file, e);
                }
            } else {
                logger.warn("Discovery Invite Token not configured. Cannot auto discover {}", this.inviteToken);
                return;
            }
        }
        try {
            SslContextFactory sslContextFactory = new SslContextFactory(true);
            httpClient = new HttpClient(sslContextFactory);
            // ProxyConfiguration proxyConfig = httpClient.getProxyConfiguration();
            // HttpProxy proxy = new HttpProxy("192.168.0.241", 8888);

            // Do not proxy requests for localhost:8080
            // proxy.getExcludedAddresses().add("192.168.0.241:8888");

            // add the new proxy to the list of proxies already registered
            // proxyConfig.getProxies().add(proxy);
            httpClient.start();
            this.gson = (new GsonBuilder()).create();

            String accessToken = this.obtainAccessToken();

            if (accessToken == null) {
                logger.warn("Unable to obtain Access Token. Auto discovery not possible");
                return;
            }

            if (this.authorizeInviteToken(accessToken)) {
                int homeId = this.retriveHomeId(accessToken);

                if (homeId != -1) {
                    HomeDTO.Light[] lights = this.retriveHomeLights(accessToken, homeId);

                    if (lights != null) {
                        for (int i = 0; i < lights.length; i++) {
                            HomeDTO.Light light = lights[i];
                            logger.debug("Found Light {} with ip {} and mac {} ", light.id, light.ip,
                                    light.mac_address);
                            this.discoveredLight(light);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Auto-Discovery of WizLighting bulbs failed {} ", e.getMessage());
            e.printStackTrace();
        }

    }

    protected String obtainAccessToken()
            throws URISyntaxException, InterruptedException, TimeoutException, ExecutionException {
        String accessToken = null;
        StringBuilder urlBuilder = new StringBuilder(WizLightingBindingConstants.DISCOVERY_API_OAUTH_URL);

        logger.debug("Result {}", urlBuilder.toString());
        Request request = httpClient.newRequest(urlBuilder.toString()).header("Authorization",
                "Basic Ml81ZHE1cHFzNnI0b3dvNDAwb284MGNvd2d3azA0Y2drb2t3azB3bzgwa3drb2M4d2NjczpNV0ppWTJFeU1Ea3dNR0V3TVdJM1pqTTFOREppWm1RMU1qTm1NV05qTVRVNE9UZzFOag==")
                .header("Content-Type", "application/x-www-form-urlencoded").method(HttpMethod.POST)
                .content(new StringContentProvider("grant_type=urn:anonymous&scope=wiz_ios"));
        logger.debug("Request is {}", request.toString());
        ContentResponse response = request.send();
        logger.debug("Oauth Result {}", response.getContentAsString());
        TokenDTO data = gson.fromJson(response.getContentAsString(), TokenDTO.class);
        if (data != null) {
            accessToken = data.access_token;
        }
        return accessToken;
    }

    protected boolean authorizeInviteToken(String accessToken)
            throws InterruptedException, TimeoutException, ExecutionException {
        AuthenticationStore a = httpClient.getAuthenticationStore();
        a.clearAuthentications();
        StringBuilder urlBuilder = new StringBuilder(
                WizLightingBindingConstants.DISCOVERY_API_INVITE_TOKEN_URL + this.inviteToken);
        logger.debug("Result {}", urlBuilder.toString());
        Request request = httpClient.newRequest(urlBuilder.toString()).header("Authorization", "Bearer " + accessToken)
                .timeout(10, TimeUnit.SECONDS);
        ContentResponse response = request.send();
        String result = response.getContentAsString();
        logger.debug("Invite Result {}", result);
        return result.contains("\"success\":true");
    }

    protected int retriveHomeId(String accessToken) throws InterruptedException, TimeoutException, ExecutionException {
        int homeId = -1;
        AuthenticationStore a = httpClient.getAuthenticationStore();
        a.clearAuthentications();
        StringBuilder urlBuilder = new StringBuilder(WizLightingBindingConstants.DISCOVERY_API_USER_DETAILS_URL);
        logger.debug("Result {}", urlBuilder.toString());
        Request request = httpClient.newRequest(urlBuilder.toString()).header("Authorization", "Bearer " + accessToken)
                .timeout(10, TimeUnit.SECONDS);
        ContentResponse response = request.send();
        String result = response.getContentAsString();
        logger.debug("User Details Result {}", result);
        UserDTO data = gson.fromJson(response.getContentAsString(), UserDTO.class);
        if (data != null) {
            homeId = data.getHomeId();
        }

        return homeId;
    }

    protected HomeDTO.Light[] retriveHomeLights(String accessToken, int homeId)
            throws InterruptedException, TimeoutException, ExecutionException {
        AuthenticationStore a = httpClient.getAuthenticationStore();
        a.clearAuthentications();
        StringBuilder urlBuilder = new StringBuilder(
                WizLightingBindingConstants.DISCOVERY_API_HOME_DETAILS_URL + homeId);
        logger.debug("Result {}", urlBuilder.toString());
        Request request = httpClient.newRequest(urlBuilder.toString()).header("Authorization", "Bearer " + accessToken)
                .timeout(10, TimeUnit.SECONDS);
        ContentResponse response = request.send();
        String result = response.getContentAsString();
        logger.debug("Home Details Result {}", result);
        HomeDTO data = gson.fromJson(response.getContentAsString(), HomeDTO.class);

        return data.getLights();

    }

    /**
     * Method called by mediator, when receive one packet from one unknown Wifi Socket.
     *
     * @param macAddress the mack address from the device.
     * @param hostAddress the host address from the device.
     */
    public void discoveredLight(final HomeDTO.Light light) {
        String macAddress = light.mac_address;
        String ipAddress = light.ip;
        int homeId = light.home_id;
        Map<String, Object> properties = new HashMap<>(2);
        properties.put(WizLightingBindingConstants.MAC_ADDRESS_ARG, macAddress);
        properties.put(WizLightingBindingConstants.HOST_ADDRESS_ARG, ipAddress);
        properties.put(WizLightingBindingConstants.HOME_ID_ARG, String.valueOf(homeId));
        ThingUID newThingId = new ThingUID(WizLightingBindingConstants.THING_TYPE_WIZ_BULB, macAddress);
        DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(newThingId).withProperties(properties)
                .withLabel("Wizlighting Bulb").withRepresentationProperty(macAddress).build();

        logger.debug("Discovered new thing with mac address '{}' and host address '{}' and homeId '{}", macAddress,
                ipAddress, homeId);

        this.thingDiscovered(discoveryResult);
    }

    // SETTERS AND GETTERS
    /**
     * Gets the {@link WizLightingMediator} of this binding.
     *
     * @return {@link WizLightingMediator}.
     */
    public WizLightingMediator getMediator() {
        return this.mediator;
    }

}
