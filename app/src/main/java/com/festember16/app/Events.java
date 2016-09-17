package com.festember16.app;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Events {

    Map<String, LatLng> locations = MainMapsActivity.allLocations;

    @SerializedName("event_id")
    public int id;

    @SerializedName("event_name")
    public String name;

    @SerializedName("event_start_time")
    public String startTime;

    @SerializedName("event_end_time")
    public String endTime;

    @SerializedName("event_venue")
    public String venue;

    @SerializedName("event_desc")
    private String description;

    @SerializedName("event_last_update_time")
    public String lastUpdateTime;

    @SerializedName("event_loc_x")
    public String locationX;

    @SerializedName("event_loc_y")
    public String locationY;

    @SerializedName("event_max_limit")
    public String maxLimit;

    @SerializedName("event_cluster")
    public String cluster;

    @SerializedName("event_date")
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLocationX() {
        setLocationXAndY();
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        setLocationXAndY();
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

    public void setLocationXAndY(){

        String location;

        //if(venue!=null) default takes care of that
        {
            switch (venue.toUpperCase()){
                case "GJCC": //OAT
                    location = MainMapsActivity.BARN_HALL;
                    break;

                case "EEE AUDI":
                    location = MainMapsActivity.EEE_AUDI;
                    break;

                case "BARN HALL":case "INFORMALS STAGE": //BARN
                    location = MainMapsActivity.BARN_HALL;
                    break;

                case "ORION":case "ORION-CHEMICAL WALL"://Orion
                    location = MainMapsActivity.ORION;
                    break;

                case "A2 HALL":
                    location = MainMapsActivity.A2_HALL;
                    break;

                case "A13 HALL":case "A13":
                    location = MainMapsActivity.A13_HALL;
                    break;

                case "PR DESK":
                    location = MainMapsActivity.ADMIN;
                    break;

                case "SAC":
                    location = MainMapsActivity.SAC;
                    break;

                case "INDIE STAGE"://LHC
                    location = MainMapsActivity.LHC;
                    break;

                case "PIXELBUG STALL"://Might change; CEESAT
                    location = MainMapsActivity.CEESAT;
                    break;

                case "BADMINTON COURT":case "NEW INDOOR STADIUM"://sports center
                    location = MainMapsActivity.SPORTS_CENTER;
                    break;


                case "NSO GREEN WALL"://NSO ground
                    location = MainMapsActivity.NSO_GROUND;
                    break;

                case "CEESAT":
                    location = MainMapsActivity.CEESAT;
                    break;

                default: //Todo: redirect to PR desk;
                    location = MainMapsActivity.ADMIN;
                    break;
            }

            assignLocations(location);
        }
    }

    private void assignLocations(String location) {
        locationX = String.valueOf(locations.get(location).longitude);
        locationY = String.valueOf(locations.get(location).latitude);
    }


}
