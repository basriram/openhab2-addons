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


/**
 * Sign In Response Object. Field names obtained from REST Service JSON structure
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
public class SignInResponse {

    private Integer code;

    private AccountInfo data;

    private String msg;

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public AccountInfo getAccountInfo() {
        return this.data;
    }

    public void setAccountInfo(AccountInfo data) {
        this.data = data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}