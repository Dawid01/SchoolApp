package com.szczepaniak.dawid.appezn;

import com.szczepaniak.dawid.appezn.Models.ClassList;
import com.szczepaniak.dawid.appezn.Models.Comment;
import com.szczepaniak.dawid.appezn.Models.CommentList;
import com.szczepaniak.dawid.appezn.Models.LessonList;
import com.szczepaniak.dawid.appezn.Models.Post;
import com.szczepaniak.dawid.appezn.Models.PostList;
import com.szczepaniak.dawid.appezn.Models.PostReaction;
import com.szczepaniak.dawid.appezn.Models.User;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    Call<Post> putPost(@Path("id") Long id, @Body Post post);

    @POST("posts")
    Call<Post> newPost(@Body Post post);

    @GET("posts")
    Call<PostList> getAllPosts(@Query("sort") String sort);



    @GET("posts")
    Call<PostList> getAllPosts(@Query("page") int page, @Query("size") int size, @Query("sort") String sort);



    @POST("reactions")
    Call<PostReaction> newPostReaction(@Body PostReaction reaction);

    @PUT("reactions/{id}")
    Call<PostReaction> putPostReaction(@Path("id") Long id, @Body PostReaction reaction);

    @POST("/reactions/post/{id}")
    Call<PostReaction> addReaction(@Body PostReaction reaction, @Path("id") Long id);


    @POST("comments")
    Call<Comment> newComment(@Body Comment comment);

    @GET("comments/post/{id}")
    Call<CommentList> getCommentsByPost(@Path("id") Long id);


    @GET("lessons")
    Call<LessonList> getLessons();


    @Multipart
    @POST("uploadFile")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);

    @GET("classes")
    Call<ClassList> getClassList(@Query("size") int size);

}
