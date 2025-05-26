package com.example.basicweatherapp; // 패키지 이름을 정확히 명시해주세요.

import com.example.basicweatherapp.model.AirQualityResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AirKoreaApiService {

    @GET("/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty")
    Call<AirQualityResponse> getRealtimeAirQuality(
            @Query("serviceKey") String serviceKey,
            @Query("returnType") String returnType,
            @Query("numOfRows") int numOfRows,
            @Query("pageNo") int pageNo,
            @Query("stationName") String stationName,
            @Query("dataTerm") String dataTerm,
            @Query("ver") String ver
    );

    @GET("/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList")
    Call<AirQualityResponse> getNearbyStations(
            @Query("serviceKey") String serviceKey, // <-- 서비스 키 추가
            @Query("returnType") String returnType, // <-- 이 파라미터로 "json" 값을 전달
            @Query("numOfRows") int numOfRows,
            @Query("pageNo") int pageNo,
            @Query("tmX") String tmX,
            @Query("tmY") String tmY
    );
}