package com.festember16.app;

import android.content.SharedPreferences;

/**
 * Created by app on 1/8/16.
 * Class to organize commonly used variables
 */


public class Utilities {

    public static String username;
    public static String password;
    public static int amount;
    public static String gender;
    public static String shirtSize;
    public static int status;

    public static SharedPreferences prefs;
    public static String base_url = "https://api.festember.com/";

    public static void init()
    {
        username = password = shirtSize = gender = null;
        amount = 0;
        status = 0;
    }
}
