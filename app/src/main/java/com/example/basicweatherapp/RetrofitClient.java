package com.example.basicweatherapp;

import com.example.basicweatherapp.AirKoreaApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String BASE_URL = "http://apis.data.go.kr";
    private static Retrofit retrofit = null;
    private static AirKoreaApiService airKoreaApiService = null;

    private RetrofitClient() {
    }

    public static AirKoreaApiService getAirKoreaApiService() {
        if (airKoreaApiService == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            airKoreaApiService = retrofit.create(AirKoreaApiService.class);
        }
        return airKoreaApiService;
    }
}