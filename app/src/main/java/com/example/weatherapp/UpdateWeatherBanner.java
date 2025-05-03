package com.example.weatherapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import android.os.Build;


public class UpdateWeatherBanner {

    public void updateWeatherBanner(String weather, LinearLayout bannerLayout, ImageView weatherIcon, TextView weatherText) {
        switch (weather) {
            case "sunny":
                weatherIcon.setImageResource(R.drawable.ic_weather_sun);
                weatherText.setText("오늘은 맑아요! 산책하기 좋은 날 ☀️");
                bannerLayout.setBackgroundColor(Color.parseColor("#FFFACD"));
                break;
            case "cloudy":
                weatherIcon.setImageResource(R.drawable.ic_weather_cloud);
                weatherText.setText("오늘은 흐려요! 겉옷을 챙기세요 ☁️");
                bannerLayout.setBackgroundColor(Color.parseColor("#D3D3D3"));
                break;
            case "rainy":
                weatherIcon.setImageResource(R.drawable.ic_weather_rain);
                weatherText.setText("오늘은 비가 와요! 우산을 챙기세요 ☔");
                bannerLayout.setBackgroundColor(Color.parseColor("#ADD8E6"));
                break;
            case "snow":
                weatherIcon.setImageResource(R.drawable.ic_weather_snow);
                weatherText.setText("오늘은 눈이 와요! 따뜻하게 입으세요 ❄️");
                bannerLayout.setBackgroundColor(Color.parseColor("#E0FFFF"));
                break;
            default:
                bannerLayout.setVisibility(View.GONE);
                break;
        }
    }

    public void showWeatherNotification(Context context, String weather) {
        // Android 8.0 이상에서 알림 채널을 만들어야 합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Weather Channel";
            String description = "Weather updates and notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("weather_channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없으면 알림 보내지 않음
                return;
            }
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        String contentText;
        int iconRes;

        switch (weather) {
            case "sunny":
                contentText = "오늘은 맑아요! 산책하기 좋은 날 ☀️";
                iconRes = R.drawable.ic_weather_sun;
                break;
            case "cloudy":
                contentText = "오늘은 흐려요! 겉옷을 챙기세요 ☁️";
                iconRes = R.drawable.ic_weather_cloud;
                break;
            case "rainy":
                contentText = "오늘은 비가 와요! 우산을 챙기세요 ☔";
                iconRes = R.drawable.ic_weather_rain;
                break;
            case "snow":
                contentText = "오늘은 눈이 와요! 따뜻하게 입으세요 ❄️";
                iconRes = R.drawable.ic_weather_snow;
                break;
            default:
                contentText = "오늘의 날씨를 확인해보세요!";
                iconRes = R.drawable.ic_weather_sun;
                break;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "weather_channel_id")
                .setSmallIcon(iconRes)
                .setContentTitle("오늘의 날씨")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(1001, builder.build());
    }

}
