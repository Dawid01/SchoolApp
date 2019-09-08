package com.szczepaniak.dawid.appezn;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    private static final String ROOT_URL = "http://192.168.0.123:8080/";
    //private static final String ROOT_URL = "http://192.168.43.224:8080/";


        private static Retrofit getRetrofitInstance() {

            OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60 * 5, TimeUnit.SECONDS)
                    .readTimeout(60 * 5, TimeUnit.SECONDS)
                    .writeTimeout(60 * 5, TimeUnit.SECONDS);
            okHttpClient.interceptors().add(new AddCookiesInterceptor());
            okHttpClient.interceptors().add(new ReceivedCookiesInterceptor());

            return new Retrofit.Builder()
                    .baseUrl(ROOT_URL)
                    .client(okHttpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }


        public static ApiService getApiService() {
            return getRetrofitInstance().create(ApiService.class);
        }
}