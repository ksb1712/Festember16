package com.mygdx.game;

import android.content.SharedPreferences;

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

    public static SharedPreferences prefs;
    public static String base_url = "http://api.festember.com";
    public static String auth_url = base_url + "/auth/app";
    public static String user_register_url = base_url + "/user/register";
    public static String event_details_url = base_url + "/events/details";
    public static String event_register = base_url + "/event/register";
    public static String user_profile = base_url + "/events/user/details";
    public static String user_qr = base_url + "/";
    public static String scoreboard_url = "https://festember.com/scoreboard/getScoreBoard";


}
