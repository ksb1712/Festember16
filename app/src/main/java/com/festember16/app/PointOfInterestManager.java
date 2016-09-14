package com.festember16.app;

// class that returns POIs

/***************************************************
 Change this Class and get the data from API Call
 ****************************************************/

import java.util.ArrayList;
import java.util.List;


public class PointOfInterestManager
{
    public static List<PointOfInterest> getEvents()
    {
        List<PointOfInterest> pois = new ArrayList<>();

        return  pois;
    }

    public static List<PointOfInterest> getHostels()
    {
        List<PointOfInterest> pois = new ArrayList<>();
        pois.add( new PointOfInterest("Garnet C", 10.763411,78.812537));
        pois.add(new PointOfInterest("Opal",10.757885 , 78.820690));
        pois.add(new PointOfInterest("Garnet B", 10.762979 ,78.811539));
        pois.add(new PointOfInterest("Garnet A", 10.762629 ,78.811543 ));
        pois.add( new PointOfInterest("Agate Hostel", 10.762061, 78.813333 ));
        pois.add(new PointOfInterest("Diamond Hostel", 10.763076, 78.814406));
        pois.add(new PointOfInterest("Coral Hostel" ,10.762161, 78.815552));
        pois.add(new PointOfInterest("Zircon A",10.766000, 78.817728));
        pois.add(new PointOfInterest("Zircon B",10.766738, 78.817728));
        pois.add(new PointOfInterest("Zircon C",10.766738, 78.817728));
        pois.add(new PointOfInterest("Lapis Hostel",10.764430, 78.813823));
        pois.add(new PointOfInterest("Emerald Hostel", 10.764430, 78.813823));
        pois.add(new PointOfInterest("Amber A", 10.767459, 78.813703));
        pois.add(new PointOfInterest("Amber B", 10.768207, 78.813687));

        pois.add(new PointOfInterest("Aquamarine A", 10.767659, 78.818407));
        pois.add(new PointOfInterest("Aquamarine B", 10.768344, 78.818402));

        pois.add(new PointOfInterest("Jasper Hostel", 10.769061, 78.818396));
        pois.add(new PointOfInterest("Sapphire Hostel", 10.765494, 78.814332));
        pois.add(new PointOfInterest("Topaz Hostel", 10.765520, 78.816312));
        pois.add(new PointOfInterest("Pearl Hostel", 10.764470, 78.815416));

        pois.add(new PointOfInterest("Ruby Hostel", 10.764533, 78.817283));
        pois.add(new PointOfInterest("Emerald Hostel", 10.763423, 78.816368));
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
        return pois;
    }
}