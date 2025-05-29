package com.example.basicweatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

public class LocationUtil {

    public interface LocationResultListener {
        void onLocationResult(double latitude, double longitude);
        void onLocationFailed(String message);
    }

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static FusedLocationProviderClient fusedLocationClient;

    public static void getCurrentLocation(Activity activity, @NonNull LocationResultListener listener) {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        }

        boolean hasFineLocation = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean hasCoarseLocation = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!hasFineLocation && !hasCoarseLocation) {
            listener.onLocationFailed("위치 권한이 필요합니다.");
            return;
        }

        // 우선 getLastLocation 시도
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, location -> {
                    if (location != null) {
                        listener.onLocationResult(location.getLatitude(), location.getLongitude());
                    } else {
                        // 실시간 위치 요청
                        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                                .setMinUpdateIntervalMillis(10000)
                                .setMaxUpdates(1)
                                .build();

                        fusedLocationClient.requestLocationUpdates(
                                locationRequest,
                                new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        if (locationResult == null || locationResult.getLastLocation() == null) {
                                            listener.onLocationFailed("실시간 위치 정보를 가져올 수 없습니다.");
                                        } else {
                                            Location loc = locationResult.getLastLocation();
                                            listener.onLocationResult(loc.getLatitude(), loc.getLongitude());
                                        }
                                        // 위치 업데이트 중지
                                        fusedLocationClient.removeLocationUpdates(this);
                                    }
                                },
                                activity.getMainLooper()
                        );
                    }
                })
                .addOnFailureListener(activity, e -> listener.onLocationFailed("위치 정보 오류: " + e.getMessage()));
    }
}
