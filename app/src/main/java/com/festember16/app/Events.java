package com.festember16.app;
import com.google.gson.annotations.SerializedName;

public class Events {

    @SerializedName("eventId")
    public int id;

    @SerializedName("eventName")
    public String name;

    @SerializedName("eventStartTime")
    public String startTime;

    @SerializedName("eventEndTime")
    public String endTime;

    @SerializedName("eventVenue")
    public String venue;

    @SerializedName("eventLastUpdateTime")
    public String lastUpdateTime;

    @SerializedName("eventLocX")
    public String locationX;

    @SerializedName("eventLocY")
    public String locationY;

    @SerializedName("eventMaxLimit")
    public String maxLimit;

    @SerializedName("eventCluster")
    public String cluster;

    @SerializedName("eventDate")
    public String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
