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

/**
 * Device List Response Object. Field names obtained from REST Service JSON structure
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
public class DeviceListResponse {

    public Integer code;

    public List<Device> data;

    public String msg;
}