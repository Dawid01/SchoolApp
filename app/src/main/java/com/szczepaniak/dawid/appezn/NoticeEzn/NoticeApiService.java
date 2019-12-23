package com.szczepaniak.dawid.appezn.NoticeEzn;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NoticeApiService {

      @GET("posts")
      Call<List<NoticePost>> getNoticePosts(@Query("page") int page);

}
