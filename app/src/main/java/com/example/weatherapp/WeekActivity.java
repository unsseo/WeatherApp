package com.example.weatherapp;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeekActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private List<WeatherData> weekList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_week);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // 요일 리스트
        String[] allDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        // 오늘 요일 구하기
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.ENGLISH);
        String today = sdf.format(calendar.getTime()); // 예: "Tue"

        // 오늘 요일 인덱스
        int todayIndex = -1;
        for (int i = 0; i < allDays.length; i++) {
            if (allDays[i].equals(today)) {
                todayIndex = i;
                break;
            }
        }

        // 주간 요일 순서 리스트 만들기
        List<String> orderedDays = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int index = (todayIndex + i) % 7;
            orderedDays.add(allDays[index]);
        }


        // 날씨 데이터 생성
        weekList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String day = orderedDays.get(i);
            String lowTemp = (14 + i) + "°C";
            String highTemp = (20 + i) + "°C";

            weekList.add(new WeatherData(day, lowTemp, highTemp, R.drawable.ic_weather_sun));
        }

        adapter = new WeatherAdapter(weekList);
        recyclerView.setAdapter(adapter);


    }

    // 팝업 다이얼로그로 차트 띄우기
}
