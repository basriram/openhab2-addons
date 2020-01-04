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

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Account Info Object. Field names obtained from REST Service JSON structure
 *
 * @author Sriram Balakrishnan - Initial contribution
 *
 */
public class AccountInfo {

    private String ab_code;

    private String auth_token;

    private String avatar;

    private String domain;

    private String email;

    private String geo_key;

    private String invitation_code;

    private String inviter_code;

    private String mac_addr;

    private String nick_name;

    private String params;

    private Integer privilege;

    private long token_expires_at;

    private String user_id;

    public String getAb_code() {
        return this.ab_code;
    }

    public void setAb_code(String ab_code) {
        this.ab_code = ab_code;
    }

    public String getAuth_token() {
        return this.auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGeo_key() {
        return this.geo_key;
    }

    public void setGeo_key(String geo_key) {
        this.geo_key = geo_key;
    }

    public String getInvitation_code() {
        return this.invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    public String getInviter_code() {
        return this.inviter_code;
    }

    public void setInviter_code(String inviter_code) {
        this.inviter_code = inviter_code;
    }

    public String getMac_addr() {
        return this.mac_addr;
    }

    public void setMac_addr(String mac_addr) {
        this.mac_addr = mac_addr;
    }

    public String getNick_name() {
        return this.nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getParams() {
        return this.params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getPrivilege() {
        return this.privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }

    public long getToken_expires_at() {
        return this.token_expires_at;
    }

    public void setToken_expires_at(long token_expires_at) {
        this.token_expires_at = token_expires_at;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}