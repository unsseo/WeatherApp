package com.example.basicweatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PrefManager {
    private static final String PREF_NAME = "app_pref";
    private static final String KEY_FIRST_LAUNCH = "first_launch";

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean result = pref.getBoolean(KEY_FIRST_LAUNCH, true);
        Log.d("PrefManager", "isFirstLaunch: " + result);
        return result;
    }

    public static void setFirstLaunch(Context context, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        pref.edit().putBoolean(KEY_FIRST_LAUNCH, value).apply();
        Log.d("PrefManager", "setFirstLaunch: " + value);
    }
}
