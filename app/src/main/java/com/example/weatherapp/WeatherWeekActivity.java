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

import java.util.ArrayList;
import java.util.List;

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

        // 시간대별 더미 온도 데이터
        List<Entry> dummyHourlyTempsMon = new ArrayList<>();
        dummyHourlyTempsMon.add(new Entry(0, 15));
        dummyHourlyTempsMon.add(new Entry(3, 16));
        dummyHourlyTempsMon.add(new Entry(6, 18));
        dummyHourlyTempsMon.add(new Entry(9, 21));
        dummyHourlyTempsMon.add(new Entry(12, 24));
        dummyHourlyTempsMon.add(new Entry(15, 23));
        dummyHourlyTempsMon.add(new Entry(18, 20));
        dummyHourlyTempsMon.add(new Entry(21, 17));

        List<Entry> dummyHourlyTempsTue = new ArrayList<>();
        dummyHourlyTempsTue.add(new Entry(0, 13));
        dummyHourlyTempsTue.add(new Entry(3, 14));
        dummyHourlyTempsTue.add(new Entry(6, 15));
        dummyHourlyTempsTue.add(new Entry(9, 18));
        dummyHourlyTempsTue.add(new Entry(12, 21));
        dummyHourlyTempsTue.add(new Entry(15, 20));
        dummyHourlyTempsTue.add(new Entry(18, 17));
        dummyHourlyTempsTue.add(new Entry(21, 15));

        // 주간 데이터 초기화
        weekList = new ArrayList<>();
        weekList.add(new WeatherData("Mon", "16°C", "24°C", R.drawable.ic_weather_sun, dummyHourlyTempsMon));
        weekList.add(new WeatherData("Tue", "15°C", "22°C", R.drawable.ic_weather_sun, dummyHourlyTempsTue));
        weekList.add(new WeatherData("Wed", "17°C", "25°C", R.drawable.ic_weather_sun, dummyHourlyTempsMon));
        weekList.add(new WeatherData("Thu", "14°C", "23°C", R.drawable.ic_weather_sun, dummyHourlyTempsTue));
        weekList.add(new WeatherData("Fri", "18°C", "27°C", R.drawable.ic_weather_sun, dummyHourlyTempsMon));
        weekList.add(new WeatherData("Sat", "16°C", "26°C", R.drawable.ic_weather_sun, dummyHourlyTempsTue));
        weekList.add(new WeatherData("Sun", "15°C", "22°C", R.drawable.ic_weather_sun, dummyHourlyTempsMon));

        adapter = new WeatherAdapter(weekList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new WeatherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WeatherData data) {
                showChartPopup(data);
            }
        });
    }

    // ✅ 팝업 다이얼로그로 차트 띄우기
    private void showChartPopup(WeatherData data) {
        Dialog dialog = new Dialog(WeatherWeekActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_chart);
        dialog.setCancelable(true);

        LineChart chart = dialog.findViewById(R.id.popup_line_chart);
        Button closeBtn = dialog.findViewById(R.id.btn_close);

        LineDataSet dataSet = new LineDataSet(data.getHourlyTemperatures(), data.getDay() + " Hourly Temps");
        dataSet.setColor(Color.BLUE);
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
