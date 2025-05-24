// WeatherUtils.java
package com.example.basicweatherapp;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeatherUtils {
    public static String getTodayWeatherType(Context context) {
        String[] allDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        int[] iconResIds = {
                R.drawable.ic_weather_sun,
                R.drawable.ic_weather_cloud,
                R.drawable.ic_weather_rain,
                R.drawable.ic_weather_snow,
                R.drawable.ic_weather_cloud,
                R.drawable.ic_weather_sun,
                R.drawable.ic_weather_rain
        };

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.ENGLISH);
        String today = sdf.format(calendar.getTime());

        int todayIndex = 0;
        for (int i = 0; i < allDays.length; i++) {
            if (allDays[i].equals(today)) {
                todayIndex = i;
                break;
            }
        }

        int todayIconResId = iconResIds[todayIndex];

        if (todayIconResId == R.drawable.ic_weather_sun) return "sunny";
        if (todayIconResId == R.drawable.ic_weather_cloud) return "cloudy";
        if (todayIconResId == R.drawable.ic_weather_rain) return "rainy";
        if (todayIconResId == R.drawable.ic_weather_snow) return "snow";
        return "sunny";
    }
}
