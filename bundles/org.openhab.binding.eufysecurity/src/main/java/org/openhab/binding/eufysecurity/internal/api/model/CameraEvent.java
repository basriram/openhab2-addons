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
package org.openhab.binding.eufysecurity.internal.api.model;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link CameraEvent} represents an event (motion or doorbell ring) received from the device
 *
 * @author Sriram Balakrishnan - Initial contribution
 */
@NonNullByDefault
public class CameraEvent {
    enum EventType {
        MOTION_EVENT,
        HUMAN_MOTION_EVENT,
        DOORBELL_EVENT
    }

    private byte @Nullable [] rawImage;
    private @Nullable String imageContentType;
    private long createdTime;
    private EventType eventType;
    private String imagePath;
    private List<String> picUrls;

    public CameraEvent(EventType eventType2, long createTime, String filePath, List<String> picUrls) {
        this.eventType = eventType2;
        this.createdTime = createTime;
        this.imagePath = filePath;
        this.rawImage = null;
        this.picUrls = picUrls;
    }

    public long getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImage(byte @Nullable [] rawImage, @Nullable String contentType) {
        this.rawImage = rawImage;
        this.imageContentType = contentType;
    }

    public @Nullable String getImageContentType() {
        return this.imageContentType;
    }

    public byte @Nullable [] getRawImage() {
        return this.rawImage;
    }

    public boolean isRawImageAvailable() {
        return rawImage != null && rawImage.length > 0;
    }

    public boolean isDoorbellEvent() {
        return this.eventType == EventType.DOORBELL_EVENT;
    }

    public boolean isMotionEvent() {
        return this.eventType == EventType.MOTION_EVENT || this.eventType == EventType.HUMAN_MOTION_EVENT;
    }

    public List<String> getPicUrls() {
        return this.picUrls;
    }
}
