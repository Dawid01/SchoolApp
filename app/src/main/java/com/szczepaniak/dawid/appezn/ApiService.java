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

    ////////////////////////////////////////////////////////////////

    @GET("posts/{id}")
    Call<Post> getPost(@Path("id") Long id);

    @PUT("posts/{id}")
    Call<Post> putPost(@Path("id") Long id, @Body User user);

    @GET("posts")
    Call<PostList> getAllPosts();

    @GET("posts/{id}")
    Call<PostList> getPosts(@Path("id") Long id);

}
