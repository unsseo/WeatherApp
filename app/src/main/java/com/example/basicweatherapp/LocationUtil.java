package com.example.basicweatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationUtil {

    public interface LocationResultListener {
        void onLocationResult(double latitude, double longitude);
        void onLocationFailed(String message);
    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    public static void getCurrentLocation(Activity activity, @NonNull LocationResultListener listener) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            listener.onLocationFailed("위치 권한이 필요합니다.");
            return;
        }
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, location -> {
                    if (location != null) {
                        listener.onLocationResult(location.getLatitude(), location.getLongitude());
                    } else {
                        listener.onLocationFailed("위치 정보를 가져올 수 없습니다.");
                    }
                });
    }
}
