// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: DeviceMessage.proto.json

package org.openhab.binding.eufysecurity.internal.api.model.generated;

public interface OTADataOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.openhab.binding.eufysecurity.internal.api.model.generated.OTAData)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.OTAState state = 1;</code>
   * @return The enum numeric value on the wire for state.
   */
  int getStateValue();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.OTAState state = 1;</code>
   * @return The state.
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.OTAState getState();

  /**
   * <code>int32 code = 2;</code>
   * @return The code.
   */
  int getCode();

  /**
   * <code>string msg = 3;</code>
   * @return The msg.
   */
  java.lang.String getMsg();
  /**
   * <code>string msg = 3;</code>
   * @return The bytes for msg.
   */
  com.google.protobuf.ByteString
      getMsgBytes();

  /**
   * <code>int32 value = 4;</code>
   * @return The value.
   */
  int getValue();
}
