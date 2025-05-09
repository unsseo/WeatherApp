package com.example.weatherapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeatherWeekActivity extends AppCompatActivity {


    private WeatherAdapter adapter;
    private List<WeatherData> weekList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView recyclerView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_week);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] allDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        int todayIndex = getTodayIndex(allDays);
        List<String> orderedDays = getOrderedDays(allDays, todayIndex);

        int[] iconResIds = {
                R.drawable.ic_weather_sun,
                R.drawable.ic_weather_cloud,
                R.drawable.ic_weather_rain,
                R.drawable.ic_weather_snow,
                R.drawable.ic_weather_rain,
                R.drawable.ic_weather_cloud,
                R.drawable.ic_weather_rain
        };

        String[] tempLows  = {"14", "13", "11", "7", "18", "9", "12"};
        String[] tempHighs = {"20", "19", "17", "12", "25", "15", "18"};
        String c = getString(R.string.celcius);

        weekList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int dayIdx = (todayIndex + i) % 7;
            weekList.add(new WeatherData(
                    orderedDays.get(i),
                    tempLows[dayIdx]+ c ,
                    tempHighs[dayIdx] + c ,
                    iconResIds[dayIdx]
            ));
        }

        adapter = new WeatherAdapter(weekList);
        recyclerView.setAdapter(adapter);

        // 오늘의 날씨 데이터 추출 (리스트 첫 번째가 오늘)
        WeatherData todayWeather = weekList.get(0);
        int todayIconResId = todayWeather.getIconResId();
        String weatherType = getWeatherTypeFromIcon(todayIconResId);

        // 알림(배너)만 노출
        UpdateWeatherBanner updater = new UpdateWeatherBanner();
        updater.showWeatherNotification(this, weatherType);
    }

    private int getTodayIndex(String[] allDays) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.ENGLISH);
        String today = sdf.format(calendar.getTime());
        for (int i = 0; i < allDays.length; i++) {
            if (allDays[i].equals(today)) return i;
        }
        return 0;
    }

    private List<String> getOrderedDays(String[] allDays, int todayIndex) {
        List<String> ordered = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int idx = (todayIndex + i) % 7;
            ordered.add(allDays[idx]);
        }
        return ordered;
    }

    // 아이콘 리소스 → 날씨 문자열 변환 함수
    private String getWeatherTypeFromIcon(int iconResId) {
        if (iconResId == R.drawable.ic_weather_sun) return "sunny";
        if (iconResId == R.drawable.ic_weather_cloud) return "cloudy";
        if (iconResId == R.drawable.ic_weather_rain) return "rainy";
        if (iconResId == R.drawable.ic_weather_snow) return "snow";
        return "sunny";
    }
}
