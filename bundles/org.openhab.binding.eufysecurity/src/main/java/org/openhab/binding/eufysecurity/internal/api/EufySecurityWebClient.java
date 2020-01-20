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
package org.openhab.binding.eufysecurity.internal.api;

import static org.openhab.binding.eufysecurity.internal.api.EufySecurityConstants.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.client.api.Request;
import org.openhab.binding.eufysecurity.internal.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openhab.binding.eufysecurity.internal.api.NotAuthorizedException;

/**
 * EufySecurity Web Client
 *
 * The {@link EufySecurityWebClient} provides basic HTTP commands to control and monitor a Eufy
 * Security device (doorbell, camera etc).
 *
 * GSON is used to provide custom deserialization on the JSON results.
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
@NonNullByDefault
public class EufySecurityWebClient {
    private final Logger logger = LoggerFactory.getLogger(EufySecurityWebClient.class);

    private static final String HEADER_ACCEPT = "*/*";
    private static final String HEADER_ACCEPT_LANGUAGE = "en-us";
    private static final String HEADER_ACCEPT_ENCODING = "br, gzip, deflate";

    private Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    private HttpClient httpClient;

    /**
     *
     * @param httpClient2
     */
    public EufySecurityWebClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Initial login to service
     *
     * @param emailAddress
     * @param password
     * @return {@code AccountInfo}
     * @throws IOException
     * @throws NotAuthorizedException
     */
    public AccountInfo login(String emailAddress, String password) throws IOException, NotAuthorizedException {
        String signIn = gson.toJson(new SignInRequest(getTransactionId(), emailAddress, password)).toString();
        logger.debug("json signin {}", signIn);
        try {
            ContentResponse response = httpClient.newRequest(PRIMARY_URL + "/passport/login").method(HttpMethod.POST)
                    .content(new StringContentProvider(signIn), "application/json").send();
            logger.debug("Response received {}", response.toString());
            if (response.getStatus() == HttpStatus.UNAUTHORIZED_401) {
                throw new NotAuthorizedException(response.getReason());
            }
            if (response.getStatus() != HttpStatus.OK_200) {
                throw new IOException(response.getReason());
            }
            SignInResponse signInResponse = (SignInResponse) gson.fromJson(response.getContentAsString(),
                    SignInResponse.class);
            return signInResponse.getAccountInfo();
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    /**
     * List all devices registered to an account
     *
     * @param token
     * @return {@link Device[]}
     * @throws IOException
     * @throws NotAuthorizedException
     */
    public Device[] getDevices(String token) throws IOException, NotAuthorizedException {
        try {
            String devReq = gson.toJson(new DeviceListRequest(getTransactionId())).toString();
            logger.debug("device list req {}", devReq);
            Request devListRequest = httpClient.newRequest(EufySecurityConstants.PRIMARY_URL + "/app/get_devs_list")
                    .method(HttpMethod.POST).content(new StringContentProvider(devReq), "application/json");

            logger.debug("before calling  request {}", devListRequest.getURI().toString());
            DeviceListResponse response = postRequest(devListRequest, token, DeviceListResponse.class);

            return (Device[]) response.data.stream().toArray(Device[]::new);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * List all stations registered to an account
     *
     * @param token
     * @return {@link Station[]}
     * @throws IOException
     * @throws NotAuthorizedException
     */
    public Station[] getStations(String token) throws IOException, NotAuthorizedException {
        try {
            String stationReq = gson.toJson(new StationListRequest(getTransactionId())).toString();
            Request stationListRequest = httpClient.newRequest(EufySecurityConstants.PRIMARY_URL + "/app/get_hub_list")
                    .method(HttpMethod.POST).content(new StringContentProvider(stationReq), "application/json");
            StationListResponse response = postRequest(stationListRequest, token, StationListResponse.class);

            return (Station[]) response.data.stream().toArray(Station[]::new);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private String getTransactionId() {
        return String.valueOf(System.currentTimeMillis());
    }

    @Nullable
    public synchronized ContentResponse downloadImage(String url) throws FileNotFoundException {
        logger.debug("Downloading image using url={}", url);
        Request request = httpClient.newRequest(url);
        request.method(HttpMethod.GET);
        request.timeout(6, TimeUnit.SECONDS);
        String errorMsg;
        try {
            ContentResponse contentResponse = request.send();
            switch (contentResponse.getStatus()) {
                case HttpStatus.OK_200:
                    return contentResponse;
                case HttpStatus.NOT_FOUND_404:
                    throw new FileNotFoundException(url);
                default:
                    errorMsg = String.format("HTTP GET failed: %d, %s", contentResponse.getStatus(),
                            contentResponse.getReason());
                    break;
            }
        } catch (TimeoutException e) {
            errorMsg = "TimeoutException: Call to Doorbird API timed out";
        } catch (ExecutionException e) {
            errorMsg = String.format("ExecutionException: %s", e.getMessage());
        } catch (InterruptedException e) {
            errorMsg = String.format("InterruptedException: %s", e.getMessage());
            Thread.currentThread().interrupt();
        }
        logger.error("Failed downloading image {}", errorMsg);
        return null;
    }

    /**
     *
     * @param <T>
     * @param url
     * @param typeOfT
     * @return
     * @throws IOException
     * @throws NotAuthorizedException
     */
    private <T> T postRequest(Request req, String token, Type typeOfT) throws IOException, NotAuthorizedException {
        return gson.fromJson(postRequest(req, token), typeOfT);
    }

    /**
     *
     * @param url
     * @return
     * @throws IOException
     * @throws NotAuthorizedException
     */
    private String postRequest(Request req, String token) throws IOException, NotAuthorizedException {
        try {
            logger.trace("Trying {}", req);
            ContentResponse response = req //
                    .header(HttpHeader.ACCEPT_LANGUAGE, HEADER_ACCEPT_LANGUAGE) //
                    .header(HttpHeader.ACCEPT_ENCODING, HEADER_ACCEPT_ENCODING) //
                    .header(HttpHeader.ACCEPT, HEADER_ACCEPT) //
                    .header("x-auth-token", token) //
                    .send();
            logger.debug("Response {}", response);
            if (response.getStatus() == HttpStatus.UNAUTHORIZED_401) {
                throw new NotAuthorizedException(response.getReason());
            }
            if (response.getStatus() != HttpStatus.OK_200) {
                throw new IOException(response.getReason());
            }
            return response.getContentAsString();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
