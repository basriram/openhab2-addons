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

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * NotAuthorizedException class
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
@SuppressWarnings("serial")
@NonNullByDefault
public class NotAuthorizedException extends Exception {
    public NotAuthorizedException(String message) {
        super(message);
    }
}