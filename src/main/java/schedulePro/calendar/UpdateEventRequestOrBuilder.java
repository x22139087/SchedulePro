// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: CalendarService.proto

package schedulePro.calendar;

public interface UpdateEventRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:calendar.UpdateEventRequest)
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
   * <code>int64 start_time = 4;</code>
   */
  long getStartTime();

  /**
   * <code>int64 end_time = 5;</code>
   */
  long getEndTime();
}
