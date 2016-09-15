package com.festember16.app;


import android.graphics.Color;
import android.location.Location;

// Model Class for all Point Of Interests

public class PointOfInterest implements Comparable<PointOfInterest>
{

    public Location location;
    public String distance;
    public double dist;
    public String Title;
    public String Description;

    public String type;

    public double left;
    public double top;

    public String eta;
    public Color eventStatus;

    PointOfInterest(String title , double lat , double lon)
    {
        this.location = new Location("gps");
        this.left = 0;
        this.top = 80;
        this.Title = title;
        this.location.setLatitude(lat);
        this.location.setLongitude(lon);
    }

    @Override
    public int compareTo(PointOfInterest o)
    {
        if( dist > o.dist)
        {
            return 1;
        }
        return -1;

    }
}
