// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: DeviceMessage.proto.json

package org.openhab.binding.eufysecurity.internal.api.model.generated;

/**
 * Protobuf enum {@code org.openhab.binding.eufysecurity.internal.api.model.generated.OTAState}
 */
public enum OTAState
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>OTA_NO_OPERATION_VALUE = 0;</code>
   */
  OTA_NO_OPERATION_VALUE(0),
  /**
   * <code>OTA_CHECKING_VERSION_VALUE = 1;</code>
   */
  OTA_CHECKING_VERSION_VALUE(1),
  /**
   * <code>OTA_DOWNLOADING_FW_VALUE = 2;</code>
   */
  OTA_DOWNLOADING_FW_VALUE(2),
  /**
   * <code>OTA_UPDATING_FW_VALUE = 3;</code>
   */
  OTA_UPDATING_FW_VALUE(3),
  /**
   * <code>OTA_UPDATE_SUCCESS_VALUE = 4;</code>
   */
  OTA_UPDATE_SUCCESS_VALUE(4),
  /**
   * <code>OTA_UPDATE_FAIL_VALUE = 5;</code>
   */
  OTA_UPDATE_FAIL_VALUE(5),
  /**
   * <code>OTA_UPDATE_BEGIN_VALUE = 6;</code>
   */
  OTA_UPDATE_BEGIN_VALUE(6),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>OTA_NO_OPERATION_VALUE = 0;</code>
   */
  public static final int OTA_NO_OPERATION_VALUE_VALUE = 0;
  /**
   * <code>OTA_CHECKING_VERSION_VALUE = 1;</code>
   */
  public static final int OTA_CHECKING_VERSION_VALUE_VALUE = 1;
  /**
   * <code>OTA_DOWNLOADING_FW_VALUE = 2;</code>
   */
  public static final int OTA_DOWNLOADING_FW_VALUE_VALUE = 2;
  /**
   * <code>OTA_UPDATING_FW_VALUE = 3;</code>
   */
  public static final int OTA_UPDATING_FW_VALUE_VALUE = 3;
  /**
   * <code>OTA_UPDATE_SUCCESS_VALUE = 4;</code>
   */
  public static final int OTA_UPDATE_SUCCESS_VALUE_VALUE = 4;
  /**
   * <code>OTA_UPDATE_FAIL_VALUE = 5;</code>
   */
  public static final int OTA_UPDATE_FAIL_VALUE_VALUE = 5;
  /**
   * <code>OTA_UPDATE_BEGIN_VALUE = 6;</code>
   */
  public static final int OTA_UPDATE_BEGIN_VALUE_VALUE = 6;


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
  public static OTAState valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static OTAState forNumber(int value) {
    switch (value) {
      case 0: return OTA_NO_OPERATION_VALUE;
      case 1: return OTA_CHECKING_VERSION_VALUE;
      case 2: return OTA_DOWNLOADING_FW_VALUE;
      case 3: return OTA_UPDATING_FW_VALUE;
      case 4: return OTA_UPDATE_SUCCESS_VALUE;
      case 5: return OTA_UPDATE_FAIL_VALUE;
      case 6: return OTA_UPDATE_BEGIN_VALUE;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<OTAState>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      OTAState> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<OTAState>() {
          public OTAState findValueByNumber(int number) {
            return OTAState.forNumber(number);
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
    return org.openhab.binding.eufysecurity.internal.api.model.generated.DeviceMessageProtoJson.getDescriptor().getEnumTypes().get(0);
  }

  private static final OTAState[] VALUES = values();

  public static OTAState valueOf(
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

  private OTAState(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:org.openhab.binding.eufysecurity.internal.api.model.generated.OTAState)
}

