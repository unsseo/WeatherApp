// PrefManager.java
package com.example.basicweatherapp;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String PREF_NAME = "app_pref";
    private static final String KEY_FIRST_LAUNCH = "first_launch";

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public static void setFirstLaunch(Context context, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        pref.edit().putBoolean(KEY_FIRST_LAUNCH, value).apply();
    }
}
