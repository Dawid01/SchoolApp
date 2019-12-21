package com.szczepaniak.dawid.appezn.NoticeEzn;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoticeRetroClient {

    private static final String ROOT_URL = "http://ezn.edu.pl/wp-json/wp/v2/";


    private static Retrofit getRetrofitInstance() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        return  new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
//
//        return new Retrofit.Builder()
//                .baseUrl(ROOT_URL)
//                .client(okHttpClient.build())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
    }


    public static NoticeApiService getApiService() {
        return getRetrofitInstance().create(NoticeApiService.class);
    }

    public static String getRootUrl() {
        return ROOT_URL;
    }
}
