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
package org.openhab.binding.eufysecurity.internal.api;

import java.io.IOException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.eclipse.smarthome.io.transport.mqtt.MqttActionCallback;
import org.eclipse.smarthome.io.transport.mqtt.MqttBrokerConnection;
import org.eclipse.smarthome.io.transport.mqtt.MqttConnectionObserver;
import org.eclipse.smarthome.io.transport.mqtt.MqttConnectionState;
import org.eclipse.smarthome.io.transport.mqtt.MqttException;
import org.eclipse.smarthome.io.transport.mqtt.MqttMessageSubscriber;

/**
 * {@link EufySecurityMqttClient} manages the MQTT connections to the Connected Device (Doorbell).
 *
 * @author Sriram Balakrishnan - Initial Contribution
 */
@NonNullByDefault
public class EufySecurityMqttClient implements MqttActionCallback {

    private final Logger logger = LoggerFactory.getLogger(EufySecurityMqttClient.class);

    private volatile @Nullable MqttBrokerConnection mqttProfileConnection;

    private volatile @Nullable CompletableFuture<Boolean> profileSubscribedFuture;

    private volatile @Nullable CompletableFuture<Boolean> profileStoppedFuture;

    private MqttMessageSubscriber messageSubscriber;
    private MqttConnectionObserver connectionObserver;

    private TrustManager trustManagers[];
    private @Nullable String clientId;

    private volatile int port = 8789;

    public EufySecurityMqttClient(String clientId, MqttMessageSubscriber messageSubscriber,
            MqttConnectionObserver connectionObserver) throws CertificateException {
        trustManagers = getTrustManagers();
        this.messageSubscriber = messageSubscriber;
        this.connectionObserver = connectionObserver;
        this.clientId = clientId;
    }

    private TrustManager[] getTrustManagers() throws CertificateException {
        ResourceBundle certificatesBundle = ResourceBundle.getBundle("eufysecurity/mqttcertificates");

        try {
            // Load server public certificates into key store
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            InputStream certificateStream;
            final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            for (String certName : certificatesBundle.keySet()) {
                certificateStream = new ByteArrayInputStream(
                        certificatesBundle.getString(certName).getBytes(StandardCharsets.UTF_8));
                X509Certificate certificate = (X509Certificate) cf.generateCertificate(certificateStream);
                keyStore.setCertificateEntry(certName, certificate);
            }

            ResourceBundle.clearCache();

            // Create trust managers used to validate server
            TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmFactory.init(keyStore);
            return tmFactory.getTrustManagers();
        } catch (CertificateException | KeyStoreException | NoSuchAlgorithmException | IOException e) {
            logger.warn("error with SSL context creation: {} ", e.getMessage());
            throw new CertificateException("SSL context creation exception", e);
        } finally {
            ResourceBundle.clearCache();
        }
    }

    /**
     * Start a secure MQTT connection and subscribe to device push topic.
     * 
     * @param regionProfile US and EU devices seem to use different servers
     * @param deviceSN Serial number of device. MQTT topic is based on serial number
     * @throws MqttException
     */
    public synchronized void startProfileConnection(String regionProfile, String deviceSN) throws MqttException {
        CompletableFuture<Boolean> future = profileStoppedFuture;
        if (future != null) {
            try {
                future.get(5000, TimeUnit.MILLISECONDS);
                logger.debug("finished stopping profile connection");
            } catch (InterruptedException | ExecutionException | TimeoutException ignore) {
                logger.debug("error stopping profile connection");
            }
            profileStoppedFuture = null;
        }

        if (isProfileConnected()) {
            logger.debug("profile already connected, no need to connect again");
            return;
        }

        logger.debug("starting profile connection...");
        String clientId = this.clientId;
        String serverAddress = EufySecurityConstants.MQTT_HOST_US;
        String serverUser = EufySecurityConstants.MQTT_US_USER_NAME;
        String serverPassword = EufySecurityConstants.MQTT_US_PASSWORD;

        if (regionProfile.equals("EU")) {
            serverAddress = EufySecurityConstants.MQTT_HOST_EU;
            serverUser = EufySecurityConstants.MQTT_EU_USER_NAME;
            serverPassword = EufySecurityConstants.MQTT_EU_PASSWORD;
        }

        MqttBrokerConnection connection = createMqttConnection(serverAddress, serverUser, serverPassword, clientId);
        connection.addConnectionObserver(connectionObserver);
        mqttProfileConnection = connection;
        try {
            if (connection.start().get(5000, TimeUnit.MILLISECONDS)) {
                if (profileSubscribedFuture == null) {
                    profileSubscribedFuture = connection
                            .subscribe(String.format(EufySecurityConstants.deviceTopic, deviceSN), messageSubscriber);
                }
            } else {
                logger.warn("error with profile password");
                throw new MqttException(4);
            }
        } catch (InterruptedException e) {
            logger.debug("profile connection interrupted exception ");
            throw new MqttException(0);
        } catch (ExecutionException e) {
            logger.debug("profile connection execution exception", e.getCause());
            throw new MqttException(32103);
        } catch (TimeoutException e) {
            logger.debug("public connection timeout exception");
            throw new MqttException(32000);
        }
    }

    private MqttBrokerConnection createMqttConnection(String serverAddress, @Nullable String username,
            @Nullable String password, @Nullable String clientId) throws MqttException {
        MqttBrokerConnection connection = new MqttBrokerConnection(serverAddress, port, true, clientId);
        connection.setTrustManagers(trustManagers);
        connection.setCredentials(username, password);
        connection.setQos(1);
        return connection;
    }

    public static String getClientId(String userId, String uniqueId) {
        String[] r1 = new String[3];
        r1[0] = "EufySecurity";
        r1[1] = userId;
        r1[2] = uniqueId;
        String format = String.format("android_%s_%s_%s", (Object[]) r1);
        return format;
    }

    /**
     * Stop the MQTT connection.
     */
    public void stopProfileConnection() {
        logger.debug("stopping device mqtt connection...");
        MqttBrokerConnection connection = mqttProfileConnection;
        if (connection != null) {
            connection.removeConnectionObserver(connectionObserver);
        }
        profileStoppedFuture = stopConnection(mqttProfileConnection);
        mqttProfileConnection = null;

        CompletableFuture<Boolean> future = profileSubscribedFuture;
        if (future != null) {
            future.complete(false);
            profileSubscribedFuture = null;
        }
    }

    private CompletableFuture<Boolean> stopConnection(@Nullable MqttBrokerConnection connection) {
        if (connection != null) {
            return connection.stop();
        } else {
            return CompletableFuture.completedFuture(true);
        }
    }

    /**
     * @return true if connection is established and subscribed to device topic
     */
    private boolean isProfileConnected() {
        return isConnected(mqttProfileConnection, profileSubscribedFuture);
    }

    private boolean isConnected(@Nullable MqttBrokerConnection brokerConnection,
            @Nullable CompletableFuture<Boolean> completableFuture) {
        MqttBrokerConnection connection = brokerConnection;
        CompletableFuture<Boolean> future = completableFuture;

        if (connection != null) {
            try {
                if ((future != null) && future.get(5000, TimeUnit.MILLISECONDS)) {
                    MqttConnectionState state = connection.connectionState();
                    logger.debug("connection state {} for {}", state, connection.getClientId());
                    return state == MqttConnectionState.CONNECTED;
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public void onSuccess(String topic) {
        logger.debug("publish succeeded {}", topic);
    }

    @Override
    public void onFailure(String topic, Throwable error) {
        logger.debug("publish failed {}, {}", topic, error.getMessage(), error);
    }
}
