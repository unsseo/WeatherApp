package com.example.basicweatherapp;

import android.Manifest;
import android.app.AlertDialog; // AlertDialog import
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.basicweatherapp.model.AirQualityResponse;
import com.example.basicweatherapp.model.Item;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailWindow extends AppCompatActivity {

    private Button dustCheckButton;
    private TextView textViewDate;
    private TextView textView_dust;       // 미세먼지 수치 표시 (예: 32)
    private TextView textView_dust_level; // 미세먼지 등급 표시 (예: 좋음)
    private static final String ORIGINAL_SERVICE_KEY = "TvzGkubKD2zFD6+MuNJPW2xNh+iJwYibMOUGm3jnYOaZZzSVJGXiak5064tvHYb4SRSGdMZHTFGeTHdKouvnxg==";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // 1. 네트워크 연결 확인
        if (!NetworkUtils.isInternetAvailable(this)) {
            Intent intent = new Intent(this, NetworkCheckActivity.class);
            intent.putExtra("returnActivity", DetailWindow.class.getName());
            startActivity(intent);
            finish();
            return;
        }

        // 2. UI 요소 초기화 (activity_details.xml에 id가 있는지 확인)
        dustCheckButton = findViewById(R.id.dust_check_button);
        textViewDate = findViewById(R.id.textView_date);
        Button homeButton = findViewById(R.id.home_button);
        textView_dust = findViewById(R.id.textView_dust);
        textView_dust_level = findViewById(R.id.textView_dust_level);

        // 3. 현재 날짜 표시
        String today = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN).format(new Date());
        textViewDate.setText(today);

        // 4. 앱 시작 시 바로 위치 권한 확인 및 미세먼지 정보 가져오기
        requestLocationAndFetchDustData();


        // 5. 미세먼지 농도 확인 버튼 클릭 리스너 (API 새로고침 + 팝업 띄우기)
        dustCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailWindow.this, "미세먼지 정보 새로고침 중...", Toast.LENGTH_SHORT).show();
                requestLocationAndFetchDustData();
                showDustPopup();
            }
        });

        // 6. 처음 화면으로 버튼 클릭 리스너
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailWindow.this, HomeScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationAndFetchDustData();
            } else {
                Toast.makeText(this, "위치 권한이 거부되었습니다. 기본 지역(강남구)으로 미세먼지 정보를 가져옵니다.", Toast.LENGTH_LONG).show();
                fetchAirQualityByStationName("강남구");
            }
        }
    }

    private void requestLocationAndFetchDustData() {
        LocationUtil.getCurrentLocation(this, new LocationUtil.LocationResultListener() {
            @Override
            public void onLocationResult(double latitude, double longitude) {
                Log.d("DetailWindow", "위도: " + latitude + ", 경도: " + longitude);
                TMConverter.TMPoint tmPoint = TMConverter.convert(latitude, longitude);
                if (tmPoint != null) {
                    Log.d("DetailWindow", "TM X: " + tmPoint.x + ", TM Y: " + tmPoint.y);
                    fetchNearbyStations(String.valueOf(tmPoint.x), String.valueOf(tmPoint.y));
                } else {
                    Log.e("DetailWindow", "TM 좌표 변환 실패");
                    Toast.makeText(DetailWindow.this, "위치 좌표 변환 실패, 기본 지역으로 가져옵니다.", Toast.LENGTH_SHORT).show();
                    fetchAirQualityByStationName("강남구");
                }
            }

            @Override
            public void onLocationFailed(String errorMessage) {
                Log.e("DetailWindow", "위치 가져오기 실패: " + errorMessage);
                Toast.makeText(DetailWindow.this, "위치 가져오기 실패: " + errorMessage + ". 기본 지역으로 가져옵니다.", Toast.LENGTH_LONG).show();
                fetchAirQualityByStationName("강남구");
            }
        });
    }
private void fetchNearbyStations(String tmX,String tmY){
        RetrofitClient.getAirKoreaApiService().getNearbyStations(
                ORIGINAL_SERVICE_KEY,
                "json",
                1,
                1,
                tmX,
                tmY
        ).enqueue(new Callback<AirQualityResponse>() {
            @Override
            public void onResponse(@NonNull Call<AirQualityResponse> call, @NonNull Response<AirQualityResponse> response) {
                if (response.isSuccessful()) {
                    AirQualityResponse airQualityResponse = response.body();
                    if (airQualityResponse != null &&
                            airQualityResponse.getResponse() != null &&
                            airQualityResponse.getResponse().getBody() != null &&
                            airQualityResponse.getResponse().getBody().getItems() != null &&
                            !airQualityResponse.getResponse().getBody().getItems().isEmpty()) {

                        Item nearbyStationItem = airQualityResponse.getResponse().getBody().getItems().get(0);
                        String stationName = nearbyStationItem.getStationName();
                        if (stationName != null && !stationName.isEmpty()) {
                            Log.d("DetailWindow", "가장 가까운 측정소: " + stationName);
                            fetchAirQualityByStationName(stationName);
                        } else {
                            Log.e("DetailWindow", "가까운 측정소 이름을 찾을 수 없습니다.");
                            Toast.makeText(DetailWindow.this, "가까운 측정소 정보를 찾을 수 없습니다. 기본 지역으로 가져옵니다.", Toast.LENGTH_SHORT).show();
                            fetchAirQualityByStationName("강남구");
                        }

                    } else {
                        Log.e("DetailWindow", "주변 측정소 데이터가 없습니다.");
                        Toast.makeText(DetailWindow.this, "주변 측정소 정보가 없습니다. 기본 지역으로 가져옵니다.", Toast.LENGTH_SHORT).show();
                        fetchAirQualityByStationName("강남구");
                    }
                } else {
                    Log.e("DetailWindow", "주변 측정소 API 호출 실패: " + response.code() + ", " + response.message());
                    Toast.makeText(DetailWindow.this, "주변 측정소 API 호출 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                    fetchAirQualityByStationName("강남구");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AirQualityResponse> call, @NonNull Throwable t) {
                Log.e("DetailWindow", "주변 측정소 API 통신 오류: " + t.getMessage(), t);
                Toast.makeText(DetailWindow.this, "네트워크 오류 (주변 측정소): " + t.getMessage(), Toast.LENGTH_LONG).show();
                fetchAirQualityByStationName("강남구");
            }
        });
    }

    // 측정소 이름으로 실시간 미세먼지 데이터를 조회하는 메서드

    private void fetchAirQualityByStationName(String stationName) {
        RetrofitClient.getAirKoreaApiService().getRealtimeAirQuality(
                ORIGINAL_SERVICE_KEY,
                "json",
                1,
                1,
                stationName,
                "DAILY",
                "1.3"
        ).enqueue(new Callback<AirQualityResponse>() {
            @Override
            public void onResponse(@NonNull Call<AirQualityResponse> call, @NonNull Response<AirQualityResponse> response) {
                if (response.isSuccessful()) {
                    AirQualityResponse airQualityResponse = response.body();
                    if (airQualityResponse != null &&
                            airQualityResponse.getResponse() != null &&
                            airQualityResponse.getResponse().getBody() != null &&
                            airQualityResponse.getResponse().getBody().getItems() != null &&
                            !airQualityResponse.getResponse().getBody().getItems().isEmpty()) {

                        Item item = airQualityResponse.getResponse().getBody().getItems().get(0);

                        runOnUiThread(() -> {
                            String pm10Value = item.getPm10Value();
                            String pm10GradeText = convertGradeToText(item.getPm10Grade());

                            if (pm10Value == null || pm10Value.isEmpty() || pm10Value.equals("-")) {
                                pm10Value = "정보 없음";
                            }
                            textView_dust.setText(pm10Value);
                            textView_dust_level.setText(pm10GradeText);
                            Log.d("DetailWindow", "측정소: " + item.getStationName() + ", PM10 값: " + pm10Value + ", PM10 등급: " + pm10GradeText);
                        });

                    } else {
                        runOnUiThread(() -> {
                            textView_dust.setText("정보 없음");
                            textView_dust_level.setText("정보 없음");
                            Log.e("DetailWindow", "미세먼지 데이터가 없거나 형식이 올바르지 않습니다.");
                            Toast.makeText(DetailWindow.this, "미세먼지 데이터 없음", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    Log.e("DetailWindow", "API 호출 실패: " + response.code() + ", " + response.message());
                    runOnUiThread(() -> Toast.makeText(DetailWindow.this, "미세먼지 API 호출 실패: " + response.code(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AirQualityResponse> call, @NonNull Throwable t) {
                Log.e("DetailWindow", "API 통신 오류: " + t.getMessage(), t);
                runOnUiThread(() -> Toast.makeText(DetailWindow.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }
    private String convertGradeToText(String grade) {
        if (grade == null || grade.isEmpty() || grade.equals("-")) return "정보 없음";
        try {
            int gradeInt = Integer.parseInt(grade);
            switch (gradeInt) {
                case 1: return "좋음";
                case 2: return "보통";
                case 3: return "나쁨";
                case 4: return "매우나쁨";
                default: return "알 수 없음";
            }
        } catch (NumberFormatException e) {
            Log.e("DetailWindow", "등급 변환 오류: " + grade, e);
            return "알 수 없음";
        }
    }

    private void showDustPopup() {
        View popupView = getLayoutInflater().inflate(R.layout.dust_popup, null); // dust_popup.xml 사용

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        popupView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, 6000);
    }
}