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
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: DeviceMessage.proto.json

package org.openhab.binding.eufysecurity.internal.api.model.generated;

public interface QuickRespItemOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.openhab.binding.eufysecurity.internal.api.model.generated.QuickRespItem)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.QuickRespState quickRespState = 1;</code>
   * @return The enum numeric value on the wire for quickRespState.
   */
  int getQuickRespStateValue();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.QuickRespState quickRespState = 1;</code>
   * @return The quickRespState.
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.QuickRespState getQuickRespState();

  /**
   * <code>string quickRespUrl = 2;</code>
   * @return The quickRespUrl.
   */
  java.lang.String getQuickRespUrl();
  /**
   * <code>string quickRespUrl = 2;</code>
   * @return The bytes for quickRespUrl.
   */
  com.google.protobuf.ByteString
      getQuickRespUrlBytes();

  /**
   * <code>int32 quickRespID = 3;</code>
   * @return The quickRespID.
   */
  int getQuickRespID();
}
