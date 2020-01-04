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
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: DeviceMessage.proto.json

package org.openhab.binding.eufysecurity.internal.api.model.generated;

/**
 * Protobuf enum {@code org.openhab.binding.eufysecurity.internal.api.model.generated.QuickRespState}
 */
public enum QuickRespState
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>QUICK_RESPONSE_UNKNOWN_VALUE = 0;</code>
   */
  QUICK_RESPONSE_UNKNOWN_VALUE(0),
  /**
   * <code>QUICK_RESPONSE_ADD_VALUE = 1;</code>
   */
  QUICK_RESPONSE_ADD_VALUE(1),
  /**
   * <code>QUICK_RESPONSE_REMOVE_VALUE = 2;</code>
   */
  QUICK_RESPONSE_REMOVE_VALUE(2),
  /**
   * <code>QUICK_RESPONSE_MODIFY_VALUE = 3;</code>
   */
  QUICK_RESPONSE_MODIFY_VALUE(3),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>QUICK_RESPONSE_UNKNOWN_VALUE = 0;</code>
   */
  public static final int QUICK_RESPONSE_UNKNOWN_VALUE_VALUE = 0;
  /**
   * <code>QUICK_RESPONSE_ADD_VALUE = 1;</code>
   */
  public static final int QUICK_RESPONSE_ADD_VALUE_VALUE = 1;
  /**
   * <code>QUICK_RESPONSE_REMOVE_VALUE = 2;</code>
   */
  public static final int QUICK_RESPONSE_REMOVE_VALUE_VALUE = 2;
  /**
   * <code>QUICK_RESPONSE_MODIFY_VALUE = 3;</code>
   */
  public static final int QUICK_RESPONSE_MODIFY_VALUE_VALUE = 3;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static QuickRespState valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static QuickRespState forNumber(int value) {
    switch (value) {
      case 0: return QUICK_RESPONSE_UNKNOWN_VALUE;
      case 1: return QUICK_RESPONSE_ADD_VALUE;
      case 2: return QUICK_RESPONSE_REMOVE_VALUE;
      case 3: return QUICK_RESPONSE_MODIFY_VALUE;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<QuickRespState>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      QuickRespState> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<QuickRespState>() {
          public QuickRespState findValueByNumber(int number) {
            return QuickRespState.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return org.openhab.binding.eufysecurity.internal.api.model.generated.DeviceMessageProtoJson.getDescriptor().getEnumTypes().get(1);
  }

  private static final QuickRespState[] VALUES = values();

  public static QuickRespState valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private QuickRespState(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:org.openhab.binding.eufysecurity.internal.api.model.generated.QuickRespState)
}

