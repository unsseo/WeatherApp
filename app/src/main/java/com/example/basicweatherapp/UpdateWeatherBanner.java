package com.example.basicweatherapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class UpdateWeatherBanner {

    private static final String CHANNEL_ID = "weather_channel_id";
    private static final String CHANNEL_NAME = "Weather Alerts";
    private static final String CHANNEL_DESC = "Weather condition notifications";
    private static final int NOTIFICATION_ID = 1001;

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription(CHANNEL_DESC);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                channel.enableVibration(true);
                manager.createNotificationChannel(channel);
                Log.d("UpdateWeatherBanner", "Notification channel created: " + CHANNEL_ID);
            } else {
                Log.d("UpdateWeatherBanner", "Notification channel already exists: " + CHANNEL_ID);
            }
        }
    }

    public void showWeatherNotification(Context context, String weather) {
        createNotificationChannel(context);

        // Android 13 이상 권한 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Log.e("UpdateWeatherBanner", "알림 권한 없음 - 알림 발송 불가");
                if (context instanceof android.app.Activity) {
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                    context.startActivity(intent);
                }
                return;
            }
        }

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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(iconRes)
                .setContentTitle("오늘의 날씨")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (!notificationManager.areNotificationsEnabled()) {
            Log.e("UpdateWeatherBanner", "알림이 시스템에서 차단됨");
            Toast.makeText(context, "앱 알림이 꺼져 있습니다. 설정에서 알림을 켜주세요.", Toast.LENGTH_LONG).show();
            if (context instanceof android.app.Activity) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                context.startActivity(intent);
            }
            return;
        }

        try {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
            Log.d("UpdateWeatherBanner", "알림 발송 성공");
        } catch (SecurityException e) {
            Log.e("UpdateWeatherBanner", "알림 발송 실패(권한 문제): " + e.getMessage());
        } catch (Exception e) {
            Log.e("UpdateWeatherBanner", "알림 발송 실패: " + e.getMessage());
        }
    }
}
