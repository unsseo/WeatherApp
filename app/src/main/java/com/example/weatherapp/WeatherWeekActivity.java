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

public class WeatherWeekActivity extends AppCompatActivity {

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

        // 더미 온도 데이터 (2세트)
        List<Entry> dummyHourlyTemps1 = new ArrayList<>();
        dummyHourlyTemps1.add(new Entry(0, 15));
        dummyHourlyTemps1.add(new Entry(3, 16));
        dummyHourlyTemps1.add(new Entry(6, 18));
        dummyHourlyTemps1.add(new Entry(9, 21));
        dummyHourlyTemps1.add(new Entry(12, 24));
        dummyHourlyTemps1.add(new Entry(15, 23));
        dummyHourlyTemps1.add(new Entry(18, 20));
        dummyHourlyTemps1.add(new Entry(21, 17));

        List<Entry> dummyHourlyTemps2 = new ArrayList<>();
        dummyHourlyTemps2.add(new Entry(0, 13));
        dummyHourlyTemps2.add(new Entry(3, 14));
        dummyHourlyTemps2.add(new Entry(6, 15));
        dummyHourlyTemps2.add(new Entry(9, 18));
        dummyHourlyTemps2.add(new Entry(12, 21));
        dummyHourlyTemps2.add(new Entry(15, 20));
        dummyHourlyTemps2.add(new Entry(18, 17));
        dummyHourlyTemps2.add(new Entry(21, 15));

        // 날씨 데이터 생성
        weekList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String day = orderedDays.get(i);
            String lowTemp = (14 + i) + "°C";
            String highTemp = (20 + i) + "°C";
            List<Entry> hourlyTemps = (i % 2 == 0) ? dummyHourlyTemps1 : dummyHourlyTemps2;

            weekList.add(new WeatherData(day, lowTemp, highTemp, R.drawable.ic_weather_sun, hourlyTemps));
        }

        adapter = new WeatherAdapter(weekList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new WeatherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WeatherData data) {
                showChartPopup(data);
            }
        });
    }

    // 팝업 다이얼로그로 차트 띄우기
    private void showChartPopup(WeatherData data) {
        Dialog dialog = new Dialog(WeatherWeekActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_chart);
        dialog.setCancelable(true);

        LineChart chart = dialog.findViewById(R.id.popup_line_chart);
        Button closeBtn = dialog.findViewById(R.id.btn_close);

        LineDataSet dataSet = new LineDataSet(data.getHourlyTemperatures(), data.getDay() + " Hourly Temps");
        dataSet.setColor(Color.GREEN);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextSize(10f);
        dataSet.setDrawCircles(true);
        dataSet.setDrawValues(true);

        chart.setData(new LineData(dataSet));
        chart.invalidate();

        closeBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
