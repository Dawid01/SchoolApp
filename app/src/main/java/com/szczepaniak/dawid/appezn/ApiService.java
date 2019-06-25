package com.szczepaniak.dawid.appezn;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface  ApiService {

    @GET("users/{id}")
    Call<User> getUser(@Path("id") Long id);

    @PUT("users/{id}")
    Call<User> putUser(@Path("id") Long id, @Body User user);

    @GET("users")
    Call<User> loginUser(@Header("Authorization") String authHeader);

    @GET("users/current/")
    Call<User> getCurrentUser();
}
