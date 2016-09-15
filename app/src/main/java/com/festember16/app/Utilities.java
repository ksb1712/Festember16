package com.festember16.app;

import android.content.SharedPreferences;

/**
 * Created by app on 1/8/16.
 * Class to organize commonly used variables
 */


public class Utilities {

    public static String username;
    public static String token;
    public static String password;

    public static int status;

    public static SharedPreferences prefs;
    public static String base_url = "https://api.festember.com/";
    public static String auth_url = "http://10.1.54.162:8000/auth/app";
    public static String user_register_url = "http://10.1.54.162:8000/user/register";
    public static String event_details_url = "http://10.1.54.162:8000/events/details";


}
