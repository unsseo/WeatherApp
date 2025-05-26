// LocationUtil.java
package com.example.basicweatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context; // 사용되지 않으므로 제거 가능
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log; // Log import 추가
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.LocationRequest; // 추가: 위치 업데이트 요청 시 필요 (아래 예시)
import com.google.android.gms.location.LocationCallback; // 추가
import com.google.android.gms.location.LocationResult; // 추가


public class LocationUtil {

    public interface LocationResultListener {
        void onLocationResult(double latitude, double longitude);
        void onLocationFailed(String message);
    }

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // FusedLocationProviderClient를 클래스 멤버로 유지하여 업데이트 요청 시 사용 가능
    private static FusedLocationProviderClient fusedLocationClient;
    private static LocationCallback locationCallback;


    public static void getCurrentLocation(Activity activity, @NonNull LocationResultListener listener) {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        }

        // 1. 권한 확인 및 요청
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, // 두 권한 모두 요청
                    LOCATION_PERMISSION_REQUEST_CODE);
            Log.d("LocationUtil", "위치 권한이 없어 요청합니다. 사용자 응답 대기 중...");
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, location -> {
                    if (location != null) {
                        Log.d("LocationUtil", "getLastLocation 성공: 위도=" + location.getLatitude() + ", 경도=" + location.getLongitude());
                        listener.onLocationResult(location.getLatitude(), location.getLongitude());
                    } else {
                        // getLastLocation이 null을 반환한 경우, 위치 업데이트를 요청하여 더 정확한 위치를 가져오려 시도
                        Log.d("LocationUtil", "getLastLocation이 null입니다. LocationUpdates를 시작합니다.");
                        requestLocationUpdates(activity, listener);
                    }
                })
                .addOnFailureListener(activity, e -> {
                    // 위치 정보 가져오기 자체에 실패한 경우 (예: LocationServices 연결 문제)
                    Log.e("LocationUtil", "위치 가져오기 실패 (OnFailureListener): " + e.getMessage(), e);
                    listener.onLocationFailed("위치 서비스 오류: " + e.getMessage());
                });
    }
    private static void requestLocationUpdates(Activity activity, @NonNull LocationResultListener listener) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // 10초마다 위치 업데이트 요청
        locationRequest.setFastestInterval(5000); // 가장 빠른 업데이트 5초
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // 높은 정확도 요구

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    Log.e("LocationUtil", "Location updates result is null.");
                    listener.onLocationFailed("위치 업데이트 정보를 가져올 수 없습니다.");
                    return;
                }
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    Log.d("LocationUtil", "Location update 성공: 위도=" + location.getLatitude() + ", 경도=" + location.getLongitude());
                    listener.onLocationResult(location.getLatitude(), location.getLongitude());
                    // 위치를 한 번만 받으려면 여기서 업데이트 중지
                    fusedLocationClient.removeLocationUpdates(this);
                } else {
                    Log.e("LocationUtil", "Location update returned null location.");
                    listener.onLocationFailed("위치 업데이트 정보가 null입니다.");
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */);
        } else {
            listener.onLocationFailed("위치 업데이트를 위한 권한이 없습니다.");
        }
    }

    public static void stopLocationUpdates() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            Log.d("LocationUtil", "Location updates stopped.");
        }
    }
}