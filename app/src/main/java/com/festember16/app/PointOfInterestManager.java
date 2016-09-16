package com.festember16.app;

// class that returns POIs

import java.util.ArrayList;
import java.util.List;


public class PointOfInterestManager
{
    public static List<PointOfInterest> getEvents()
    {
        List<PointOfInterest> pois = new ArrayList<>();
        pois.add( new PointOfInterest("Orion", 10.759723, 78.810776));
        pois.add( new PointOfInterest("Barn Hall", 10.759274, 78.813343));
        pois.add( new PointOfInterest("EEE Auditorium", 10.759012, 78.814704));
        pois.add( new PointOfInterest("A13 Hall", 10.758994, 78.813629));
        pois.add( new PointOfInterest("A2 Hall", 10.758977, 78.812899));
        pois.add( new PointOfInterest("Informal\'s Stage", 10.759597, 78.813574));
        pois.add( new PointOfInterest("CEESAT Ground", 10.760900, 78.812702));
        pois.add( new PointOfInterest("GJCC", 10.761548, 78.811584));
        pois.add( new PointOfInterest("SAC", 10.756136, 78.815626));
        pois.add( new PointOfInterest("Orion Chemical Wall", 10.759545, 78.810211));
        pois.add( new PointOfInterest("Pixelbug Stall", 10.759772, 78.812368));
        pois.add( new PointOfInterest("Badminton Court", 10.757776, 78.816631));
        pois.add( new PointOfInterest("NSO Green Wall", 10.756935, 78.814305));
        pois.add( new PointOfInterest("Indie Stage", 10.761060, 78.814041));
        pois.add( new PointOfInterest("New Indoor Stadium", 10.757109, 78.816499));
        return  pois;
    }

    public static List<PointOfInterest> getHostels()
    {
        List<PointOfInterest> pois = new ArrayList<>();
        pois.add( new PointOfInterest("Garnet", 10.762816, 78.811601));
        pois.add(new PointOfInterest("Opal",10.757885 , 78.820690));
        pois.add( new PointOfInterest("Agate Hostel", 10.762061, 78.813333 ));
        pois.add(new PointOfInterest("Diamond Hostel", 10.763076, 78.814406));
        pois.add(new PointOfInterest("Coral Hostel" ,10.762161, 78.815552));
        pois.add(new PointOfInterest("Zircon",10.766522, 78.817156));
        pois.add(new PointOfInterest("Lapis Hostel",10.764430, 78.813823));
        pois.add(new PointOfInterest("Emerald Hostel", 10.764430, 78.813823));
        pois.add(new PointOfInterest("Amber", 10.767778, 78.813532));
        pois.add(new PointOfInterest("Aquamarine", 10.768048, 78.817960));
        pois.add(new PointOfInterest("Jasper Hostel", 10.769061, 78.818396));
        pois.add(new PointOfInterest("Sapphire Hostel", 10.765494, 78.814332));
        pois.add(new PointOfInterest("Topaz Hostel", 10.765520, 78.816312));
        pois.add(new PointOfInterest("Pearl Hostel", 10.764470, 78.815416));
        pois.add(new PointOfInterest("Ruby Hostel", 10.764533, 78.817283));
        //pois.add(new PointOfInterest("Emerald Hostel", 10.763423, 78.816368));
        pois.add(new PointOfInterest("Jade Hostel", 10.763449, 78.817891));
        pois.add(new PointOfInterest("Beryl Hostel", 10.762283, 78.817482));
        return pois;
    }

    public static List<PointOfInterest> getUtils()
    {
        List<PointOfInterest> pois = new ArrayList<>();
        pois.add(new PointOfInterest("SBI ATM", 10.759389, 78.814166 ));
        pois.add(new PointOfInterest("NITT Hospital" ,10.762398, 78.818537));
        pois.add(new PointOfInterest("SBI ATM", 10.760916, 78.818987));
        pois.add(new PointOfInterest("Shopping Complex", 10.761164, 78.818831));
        //10.758859, 78.813254
        pois.add(new PointOfInterest("PR Desk", 10.758859, 78.813254));
        return pois;
    }
}