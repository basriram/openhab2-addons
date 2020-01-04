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

import static org.openhab.binding.eufysecurity.internal.api.EufySecurityConstants.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.eufysecurity.internal.api.NotAuthorizedException;
import org.openhab.binding.eufysecurity.internal.api.model.generated.DeviceSettingMessage;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.smarthome.core.common.ThreadPoolManager;
import org.openhab.binding.eufysecurity.internal.ImageReadyListener;
import org.openhab.binding.eufysecurity.internal.api.EufySecurityWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link EufySecuritySystem} Primary Bridge to obtain devices and hubs registered to
 * the user account .
 *
 * @author Sriram Balakrishnan - Initial contribution
 */
@NonNullByDefault
public class EufySecuritySystem {
    private final Logger logger = LoggerFactory.getLogger(EufySecuritySystem.class);

    private final EufySecurityWebClient client;
    private AccountInfo accountInfo = null;
    private String userId = null;
    private final Map<String, Device> deviceMapNew = new HashMap<>();
    private final Map<String, Station> stationMapNew = new HashMap<>();
    private final ScheduledExecutorService imageDownloadScheduler = ThreadPoolManager
            .getScheduledPool("eufysecurityimagethread");
    private java.util.concurrent.ScheduledFuture<?> imageDownloadJob;
    private boolean connected = false;
    private String region = "US";

    public EufySecuritySystem(final org.eclipse.jetty.client.HttpClient httpClient2) {
        client = new EufySecurityWebClient(httpClient2);
    }

    /**
     * Establishes the connection to the eufy security network. The
     * caller has to handle the retry to establish the connection if the method
     * returns {@code false}.
     *
     * @param emailAddress email address used to authenticate the user
     * @param password passwordused to authenticate the user
     * @return {@code true} if connection is established else returns {@code false}
     */
    public boolean establishConnection(final String emailAddress, final String password)
            throws IOException, NotAuthorizedException {
        connected = false;

        try {
            this.accountInfo = client.login(emailAddress, password);
            logger.debug("Obtained Account Information {}", accountInfo.toString());

            if (accountInfo.getAuth_token() == null) {
                throw new IOException("Response from server not valid");
            } else {
                this.userId = accountInfo.getUser_id();
                connected = true;
            }

        } catch (final IOException e) {
            logger.debug("Exception during connection to bridge with message: {}", e.getMessage());
            logger.debug("Could not connect to server using email address {}", emailAddress);
            connected = false;
        }

        return connected;
    }

    public void closeConnection() {
        logger.debug("Shutting down any pending image download jobs");
        if (imageDownloadJob != null)
            imageDownloadJob.cancel(true);
    }

    /**
     * This method returns a {@code Map<String devicesn, Device eufyDevice>} with all
     * device found registered to the account after an connection to the system is established
     * via a bridge.
     * 
     * @return a HashMap with all Eufy Devices registered to the user
     */
    public synchronized Map<String, Device> getAllDevices() {
        deviceMapNew.clear();
        try {
            final Device[] devices = client.getDevices(accountInfo.getAuth_token());

            final boolean resultIsEmpty = (devices == null || devices.length == 0);

            if (resultIsEmpty) {
                logger.error("eufysecurity System found no devices.");
            }

            for (int i = 0; i < devices.length; i++) {
                deviceMapNew.put(devices[i].device_sn, devices[i]);
            }
        } catch (IOException | NotAuthorizedException e) {
            logger.error("error while getting device list {}", e.getLocalizedMessage());
        }

        return deviceMapNew;
    }

    /**
     * This method returns all Eufy Hub/Stations registered to the user
     * a {@code Map<String stationsn, Station eufyStation>}. Before calling this method a
     * connection via a bridge has to be established
     *
     * @return a HashMap with all Eufy Device Hubs
     */
    public synchronized Map<String, Station> getAllStations() {
        stationMapNew.clear();
        try {
            final Station[] stations = client.getStations(accountInfo.getAuth_token());

            final boolean resultIsEmpty = (stations == null || stations.length == 0);

            if (resultIsEmpty) {
                logger.error("eufysecurity System found no stations.");
            }

            for (int i = 0; i < stations.length; i++) {
                stationMapNew.put(stations[i].station_sn, stations[i]);
            }
        } catch (IOException | NotAuthorizedException e) {
            logger.error("error while getting station list {}", e.getLocalizedMessage());
        }

        return stationMapNew;
    }

    protected void scheduleImageDownloadJob(CameraEvent event, ImageReadyListener listener) {
        if (event.getPicUrls() != null && event.getPicUrls().size() > 0) {
            final String picurl = event.getPicUrls().get(0); // Get the first image only for now
            try {
                final ContentResponse response = client.downloadImage(picurl);
                event.setImage(response.getContent(), response.getMediaType());
                listener.imageReady(event);
            } catch (final FileNotFoundException fe) {
                logger.warn("Picture not ready yet..need to wait longer");
            }
        }

    }

    /**
     * Method to parse a notification packet from device and schedule a image download job.
     * Doorbell events arrive faster than the image so need to delay the image download.
     * {@code ImageReadyListener} will be notified once image is downloaded
     *
     * @param eventMessage eufy device notification packet from MQTT payload
     * @param imageReadyListener listener that needs to be notified when image is downloaded
     * @return {@code CameraEvent} cameraEvent or null
     */

    public CameraEvent parseCameraEvent(final DeviceSettingMessage eventMessage,
            final ImageReadyListener imageReadyListener) {
        CameraEvent cameraEvent = null;
        try {
            final int msgTypeValue = eventMessage.getMsgTypeValue();
            if (msgTypeValue != 3101 && msgTypeValue != 3102 && msgTypeValue != 3103) {
                logger.warn("Message type does not match any camera event types {}", msgTypeValue);
            } else {
                final String filePath = eventMessage.getNotify().getNotificationData().getFilePath(); // location of
                                                                                                      // local folder on
                                                                                                      // camera with
                                                                                                      // video
                // final int cipher = eventMessage.getNotify().getNotificationData().getCipher();
                // final int channel = eventMessage.getNotify().getNotificationData().getChannel();
                final long createTime = eventMessage.getNotify().getNotificationData().getCreateTime();
                final List<String> picUrls = eventMessage.getNotify().getNotificationData().getPicUrlList();
                CameraEvent.EventType eventType = null;
                switch (msgTypeValue) {
                    case 3101:
                        eventType = CameraEvent.EventType.MOTION_EVENT;
                        break;
                    case 3102:
                        eventType = CameraEvent.EventType.HUMAN_MOTION_EVENT;
                        break;
                    default:
                        eventType = CameraEvent.EventType.DOORBELL_EVENT;
                }
                cameraEvent = new CameraEvent(eventType, createTime, filePath, picUrls);
                final CameraEvent picEvent = cameraEvent;
                imageDownloadJob = imageDownloadScheduler.schedule(() -> {
                    logger.debug("invoking thread to download picurls for event ");
                    scheduleImageDownloadJob(picEvent, imageReadyListener);
                }, IMAGE_DOWNLOAD_DELAY, TimeUnit.SECONDS);
            }

        } catch (final Exception e) {
            logger.error("Unable to parse device setting message {}", eventMessage);
        }
        return cameraEvent;
    }

    public Map<String, Device> getDeviceMap() {
        return deviceMapNew;
    }

    public Map<String, Station> getStationMap() {
        return stationMapNew;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }
}
