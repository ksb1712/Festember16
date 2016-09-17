package com.festember16.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import java.util.List;

/**
 * Created by app on 1/8/16.
 * Class to organize commonly used variables
 */


public class Utilities {

    public static String username;
    public static int qr_status;
    public static String token;
    public static String password;

    public static int status;
    public static String registered_events;
    public static SharedPreferences prefs;
    public static String base_url = "http://api.festember.com";
    public static String auth_url = base_url + "/auth/app";
    public static String user_register_url = base_url + "/user/register";
    public static String event_details_url = base_url + "/events/details";
    public static String event_register = base_url + "/events/register";
    public static String user_profile = base_url + "/events/user/details";
    public static String user_qr = base_url + "/tshirt/qr";
    public static String scoreboard_url = "https://festember.com/scoreboard/getScoreBoard";
    public static Context context;
    public static String user_id;
    public static String[] map_events = new String[1000];
    public static String[] clusters = new String[15];
    public static  List<Events> events;
    public static int global_index = 0;
    public static Events single_event;
    public FragmentDay0 fragmentDay0;
    public FragmentDay1 fragmentDay1;
    public FragmentDay2 fragmentDay2;
    public FragmentDay3 fragmentDay3;
   // public static Typeface typeface = Typeface.createFromAsset(getAss)

}
