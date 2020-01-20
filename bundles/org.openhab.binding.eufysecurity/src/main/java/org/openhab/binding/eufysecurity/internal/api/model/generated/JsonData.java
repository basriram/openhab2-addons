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

/**
 * Protobuf type {@code org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData}
 */
public  final class JsonData extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData)
    JsonDataOrBuilder {
private static final long serialVersionUID = 0L;
  // Use JsonData.newBuilder() to construct.
  private JsonData(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private JsonData() {
    jsonString_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new JsonData();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private JsonData(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            jsonString_ = s;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.openhab.binding.eufysecurity.internal.api.model.generated.DeviceMessageProtoJson.internal_static_org_openhab_binding_eufysecurity_internal_api_model_generated_JsonData_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.openhab.binding.eufysecurity.internal.api.model.generated.DeviceMessageProtoJson.internal_static_org_openhab_binding_eufysecurity_internal_api_model_generated_JsonData_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData.class, org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData.Builder.class);
  }

  public static final int JSONSTRING_FIELD_NUMBER = 1;
  private volatile java.lang.Object jsonString_;
  /**
   * <code>string jsonString = 1;</code>
   * @return The jsonString.
   */
  public java.lang.String getJsonString() {
    java.lang.Object ref = jsonString_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      jsonString_ = s;
      return s;
    }
  }
  /**
   * <code>string jsonString = 1;</code>
   * @return The bytes for jsonString.
   */
  public com.google.protobuf.ByteString
      getJsonStringBytes() {
    java.lang.Object ref = jsonString_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      jsonString_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getJsonStringBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, jsonString_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getJsonStringBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, jsonString_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData)) {
      return super.equals(obj);
    }
    org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData other = (org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData) obj;

    if (!getJsonString()
        .equals(other.getJsonString())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + JSONSTRING_FIELD_NUMBER;
    hash = (53 * hash) + getJsonString().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData)
      org.openhab.binding.eufysecurity.internal.api.model.generated.JsonDataOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.openhab.binding.eufysecurity.internal.api.model.generated.DeviceMessageProtoJson.internal_static_org_openhab_binding_eufysecurity_internal_api_model_generated_JsonData_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.openhab.binding.eufysecurity.internal.api.model.generated.DeviceMessageProtoJson.internal_static_org_openhab_binding_eufysecurity_internal_api_model_generated_JsonData_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData.class, org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData.Builder.class);
    }

    // Construct using org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      jsonString_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.openhab.binding.eufysecurity.internal.api.model.generated.DeviceMessageProtoJson.internal_static_org_openhab_binding_eufysecurity_internal_api_model_generated_JsonData_descriptor;
    }

    @java.lang.Override
    public org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData getDefaultInstanceForType() {
      return org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData.getDefaultInstance();
    }

    @java.lang.Override
    public org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData build() {
      org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData buildPartial() {
      org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData result = new org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData(this);
      result.jsonString_ = jsonString_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData) {
        return mergeFrom((org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData other) {
      if (other == org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData.getDefaultInstance()) return this;
      if (!other.getJsonString().isEmpty()) {
        jsonString_ = other.jsonString_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object jsonString_ = "";
    /**
     * <code>string jsonString = 1;</code>
     * @return The jsonString.
     */
    public java.lang.String getJsonString() {
      java.lang.Object ref = jsonString_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        jsonString_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string jsonString = 1;</code>
     * @return The bytes for jsonString.
     */
    public com.google.protobuf.ByteString
        getJsonStringBytes() {
      java.lang.Object ref = jsonString_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        jsonString_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string jsonString = 1;</code>
     * @param value The jsonString to set.
     * @return This builder for chaining.
     */
    public Builder setJsonString(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      jsonString_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string jsonString = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearJsonString() {
      
      jsonString_ = getDefaultInstance().getJsonString();
      onChanged();
      return this;
    }
    /**
     * <code>string jsonString = 1;</code>
     * @param value The bytes for jsonString to set.
     * @return This builder for chaining.
     */
    public Builder setJsonStringBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      jsonString_ = value;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData)
  }

  // @@protoc_insertion_point(class_scope:org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData)
  private static final org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData();
  }

  public static org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<JsonData>
      PARSER = new com.google.protobuf.AbstractParser<JsonData>() {
    @java.lang.Override
    public JsonData parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new JsonData(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<JsonData> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<JsonData> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.openhab.binding.eufysecurity.internal.api.model.generated.JsonData getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

