// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ReminderService.proto

package schedulePro.reminder;

public interface ReminderOrBuilder extends
    // @@protoc_insertion_point(interface_extends:schedulePro.reminder.Reminder)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string id = 1;</code>
   */
  java.lang.String getId();
  /**
   * <code>string id = 1;</code>
   */
  com.google.protobuf.ByteString
      getIdBytes();

  /**
   * <code>string title = 2;</code>
   */
  java.lang.String getTitle();
  /**
   * <code>string title = 2;</code>
   */
  com.google.protobuf.ByteString
      getTitleBytes();

  /**
   * <code>string description = 3;</code>
   */
  java.lang.String getDescription();
  /**
   * <code>string description = 3;</code>
   */
  com.google.protobuf.ByteString
      getDescriptionBytes();

  /**
   * <code>int64 time = 4;</code>
   */
  long getTime();

  /**
   * <code>string userId = 5;</code>
   */
  java.lang.String getUserId();
  /**
   * <code>string userId = 5;</code>
   */
  com.google.protobuf.ByteString
      getUserIdBytes();
}