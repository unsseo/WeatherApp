package com.example.basicweatherapp;

import androidx.annotation.NonNull;
import android.util.Log;

public class TMConverter {

    private static final double RE = 6371.00877; // 지구 반경 (km)
    private static final double GRID = 5.0;     // 격자 간격 (km)
    private static final double SLAT1 = 30.0;   // 표준 위도 1 (도)
    private static final double SLAT2 = 60.0;   // 표준 위도 2 (도)
    private static final double OLON = 126.0;   // 기준 경도 (도)
    private static final double OLAT = 38.0;    // 기준 위도 (도)
    private static final double XO = 43.0;      // 원점 X좌표 (km)
    private static final double YO = 136.0;     // 원점 Y좌표 (km)

    public static class TMPoint {
        public double x;
        public double y;

        public TMPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @NonNull
        @Override
        public String toString() {
            return "TMPoint{x=" + x + ", y=" + y + '}';
        }
    }

    public static TMPoint convert(double lat, double lon) {
        try {
            double DEGRAD = Math.PI / 180.0;
            // double RADDEG = 180.0 / Math.PI; // 사용되지 않음

            double re = RE / GRID;
            double slat1 = SLAT1 * DEGRAD;
            double slat2 = SLAT2 * DEGRAD;
            double olon = OLON * DEGRAD;
            double olat = OLAT * DEGRAD;

            double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
            sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
            double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
            sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
            double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
            ro = re * sf / Math.pow(ro, sn);

            double ra = Math.tan(Math.PI * 0.25 + lat * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lon * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;

            double x = ra * Math.sin(theta) + XO;
            double y = ro - ra * Math.cos(theta) + YO;

            return new TMPoint(x, y);
        } catch (Exception e) {
            Log.e("TMConverter", "TM 좌표 변환 중 오류 발생: " + e.getMessage(), e);
            return null;
        }
    }
}
