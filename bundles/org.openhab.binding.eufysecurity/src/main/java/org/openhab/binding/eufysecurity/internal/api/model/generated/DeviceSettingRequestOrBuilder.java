// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: DeviceMessage.proto.json

package org.openhab.binding.eufysecurity.internal.api.model.generated;

public interface DeviceSettingRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.openhab.binding.eufysecurity.internal.api.model.generated.DeviceSettingRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>bool boolValue = 10;</code>
   * @return The boolValue.
   */
  boolean getBoolValue();

  /**
   * <code>int32 intValue = 11;</code>
   * @return The intValue.
   */
  int getIntValue();

  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.ActiveZone activeZone = 16;</code>
   * @return Whether the activeZone field is set.
   */
  boolean hasActiveZone();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.ActiveZone activeZone = 16;</code>
   * @return The activeZone.
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.ActiveZone getActiveZone();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.ActiveZone activeZone = 16;</code>
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.ActiveZoneOrBuilder getActiveZoneOrBuilder();

  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.OTAData otaData = 19;</code>
   * @return Whether the otaData field is set.
   */
  boolean hasOtaData();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.OTAData otaData = 19;</code>
   * @return The otaData.
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.OTAData getOtaData();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.OTAData otaData = 19;</code>
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.OTADataOrBuilder getOtaDataOrBuilder();

  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.MotionDetectionMode motionDetectionMode = 21;</code>
   * @return The enum numeric value on the wire for motionDetectionMode.
   */
  int getMotionDetectionModeValue();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.MotionDetectionMode motionDetectionMode = 21;</code>
   * @return The motionDetectionMode.
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.MotionDetectionMode getMotionDetectionMode();

  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.HistoryDeleteData historyDeleteData = 22;</code>
   * @return Whether the historyDeleteData field is set.
   */
  boolean hasHistoryDeleteData();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.HistoryDeleteData historyDeleteData = 22;</code>
   * @return The historyDeleteData.
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.HistoryDeleteData getHistoryDeleteData();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.HistoryDeleteData historyDeleteData = 22;</code>
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.HistoryDeleteDataOrBuilder getHistoryDeleteDataOrBuilder();

  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.QuickRespData quickRespData = 23;</code>
   * @return Whether the quickRespData field is set.
   */
  boolean hasQuickRespData();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.QuickRespData quickRespData = 23;</code>
   * @return The quickRespData.
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.QuickRespData getQuickRespData();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.QuickRespData quickRespData = 23;</code>
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.QuickRespDataOrBuilder getQuickRespDataOrBuilder();

  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData jsonData = 24;</code>
   * @return Whether the jsonData field is set.
   */
  boolean hasJsonData();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData jsonData = 24;</code>
   * @return The jsonData.
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData getJsonData();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData jsonData = 24;</code>
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.JsonDataOrBuilder getJsonDataOrBuilder();

  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.MediaStreamInfo mediaStreamInfo = 25;</code>
   * @return Whether the mediaStreamInfo field is set.
   */
  boolean hasMediaStreamInfo();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.MediaStreamInfo mediaStreamInfo = 25;</code>
   * @return The mediaStreamInfo.
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.MediaStreamInfo getMediaStreamInfo();
  /**
   * <code>.org.openhab.binding.eufysecurity.internal.api.model.generated.MediaStreamInfo mediaStreamInfo = 25;</code>
   */
  org.openhab.binding.eufysecurity.internal.api.model.generated.MediaStreamInfoOrBuilder getMediaStreamInfoOrBuilder();

  /**
   * <code>string opId = 3;</code>
   * @return The opId.
   */
  java.lang.String getOpId();
  /**
   * <code>string opId = 3;</code>
   * @return The bytes for opId.
   */
  com.google.protobuf.ByteString
      getOpIdBytes();

  public org.openhab.binding.eufysecurity.internal.api.model.generated.DeviceSettingRequest.ReqDataCase getReqDataCase();
}
