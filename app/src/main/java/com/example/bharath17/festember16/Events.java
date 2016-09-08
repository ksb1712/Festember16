package com.example.bharath17.festember16;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Events {

@SerializedName("event_id")
@Expose
private String eventId;
@SerializedName("event_name")
@Expose
private String eventName;
@SerializedName("event_start_time")
@Expose
private String eventStartTime;
@SerializedName("event_end_time")
@Expose
private String eventEndTime;
@SerializedName("event_venue")
@Expose
private String eventVenue;
@SerializedName("event_last_update_time")
@Expose
private String eventLastUpdateTime;
@SerializedName("event_loc_x")
@Expose
private String eventLocX;
@SerializedName("event_loc_y")
@Expose
private String eventLocY;
@SerializedName("event_max_limit")
@Expose
private String eventMaxLimit;
@SerializedName("event_cluster")
@Expose
private String eventCluster;
@SerializedName("event_date")
@Expose
private String eventDate;

/**
* No args constructor for use in serialization
*
*/
public Events() {
}

/**
*
* @param eventMaxLimit
* @param eventVenue
* @param eventDate
* @param eventLocX
* @param eventLocY
* @param eventId
* @param eventLastUpdateTime
* @param eventEndTime
* @param eventStartTime
* @param eventName
* @param eventCluster
*/
public Events(String eventId, String eventName, String eventStartTime, String eventEndTime, String eventVenue, String eventLastUpdateTime, String eventLocX, String eventLocY, String eventMaxLimit, String eventCluster, String eventDate) {
this.eventId = eventId;
this.eventName = eventName;
this.eventStartTime = eventStartTime;
this.eventEndTime = eventEndTime;
this.eventVenue = eventVenue;
this.eventLastUpdateTime = eventLastUpdateTime;
this.eventLocX = eventLocX;
this.eventLocY = eventLocY;
this.eventMaxLimit = eventMaxLimit;
this.eventCluster = eventCluster;
this.eventDate = eventDate;
}

/**
*
* @return
* The eventId
*/
public String getEventId() {
return eventId;
}

/**
*
* @param eventId
* The event_id
*/
public void setEventId(String eventId) {
this.eventId = eventId;
}

/**
*
* @return
* The eventName
*/
public String getEventName() {
return eventName;
}

/**
*
* @param eventName
* The event_name
*/
public void setEventName(String eventName) {
this.eventName = eventName;
}

/**
*
* @return
* The eventStartTime
*/
public String getEventStartTime() {
return eventStartTime;
}

/**
*
* @param eventStartTime
* The event_start_time
*/
public void setEventStartTime(String eventStartTime) {
this.eventStartTime = eventStartTime;
}

/**
*
* @return
* The eventEndTime
*/
public String getEventEndTime() {
return eventEndTime;
}

/**
*
* @param eventEndTime
* The event_end_time
*/
public void setEventEndTime(String eventEndTime) {
this.eventEndTime = eventEndTime;
}

/**
*
* @return
* The eventVenue
*/
public String getEventVenue() {
return eventVenue.toUpperCase();
}

/**
*
* @param eventVenue
* The event_venue
*/
public void setEventVenue(String eventVenue) {
this.eventVenue = eventVenue;
}

/**
*
* @return
* The eventLastUpdateTime
*/
public String getEventLastUpdateTime() {
return eventLastUpdateTime;
}

/**
*
* @param eventLastUpdateTime
* The event_last_update_time
*/
public void setEventLastUpdateTime(String eventLastUpdateTime) {
this.eventLastUpdateTime = eventLastUpdateTime;
}

/**
*
* @return
* The eventLocX
*/
public String getEventLocX() {
return eventLocX;
}

/**
*
* @param eventLocX
* The event_loc_x
*/
public void setEventLocX(String eventLocX) {
this.eventLocX = eventLocX;
}

/**
*
* @return
* The eventLocY
*/
public String getEventLocY() {
return eventLocY;
}

/**
*
* @param eventLocY
* The event_loc_y
*/
public void setEventLocY(String eventLocY) {
this.eventLocY = eventLocY;
}

/**
*
* @return
* The eventMaxLimit
*/
public String getEventMaxLimit() {
return eventMaxLimit;
}

/**
*
* @param eventMaxLimit
* The event_max_limit
*/
public void setEventMaxLimit(String eventMaxLimit) {
this.eventMaxLimit = eventMaxLimit;
}

/**
*
* @return
* The eventCluster
*/
public String getEventCluster() {
return eventCluster;
}

/**
*
* @param eventCluster
* The event_cluster
*/
public void setEventCluster(String eventCluster) {
this.eventCluster = eventCluster;
}

/**
*
* @return
* The eventDate
*/
public String getEventDate() {
return eventDate;
}

/**
*
* @param eventDate
* The event_date
*/
public void setEventDate(String eventDate) {
this.eventDate = eventDate;
}

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

@Override
public int hashCode() {
return new HashCodeBuilder().append(eventId).append(eventName).append(eventStartTime).append(eventEndTime).append(eventVenue).append(eventLastUpdateTime).append(eventLocX).append(eventLocY).append(eventMaxLimit).append(eventCluster).append(eventDate).toHashCode();
}

@Override
public boolean equals(Object other) {
if (other == this) {
return true;
}
if ((other instanceof Events) == false) {
return false;
}
Events rhs = ((Events) other);
return new EqualsBuilder().append(eventId, rhs.eventId).append(eventName, rhs.eventName).append(eventStartTime, rhs.eventStartTime).append(eventEndTime, rhs.eventEndTime).append(eventVenue, rhs.eventVenue).append(eventLastUpdateTime, rhs.eventLastUpdateTime).append(eventLocX, rhs.eventLocX).append(eventLocY, rhs.eventLocY).append(eventMaxLimit, rhs.eventMaxLimit).append(eventCluster, rhs.eventCluster).append(eventDate, rhs.eventDate).isEquals();
}

}
