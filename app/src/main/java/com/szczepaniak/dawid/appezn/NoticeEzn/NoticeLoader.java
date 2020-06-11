package com.szczepaniak.dawid.appezn.NoticeEzn;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeLoader {

    private RecyclerView recyclerView;
    private NoticeAdapter noticeAdapter;
    private int page = 1;
    private ArrayList<NoticePost> rowsArrayList;
    private boolean isLoading = false;
    private NoticeApiService api;
    private Context context;
    private SwipeRefreshLayout refreshLayout;

    public NoticeLoader(RecyclerView recyclerView, NoticeApiService api, SwipeRefreshLayout refreshLayout, Context context) {
        this.recyclerView = recyclerView;
        this.api = api;
        this.refreshLayout = refreshLayout;
        this.context = context;
        rowsArrayList = new ArrayList<>();
        populateData();
        initAdapter();
        initScrollListener();
    }

    private void populateData() {

        setPostList(page);
    }

    private void initAdapter() {

        noticeAdapter = new NoticeAdapter(rowsArrayList, context);
        noticeAdapter.setHasStableIds(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(noticeAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (!isLoading) {
                    if (!recyclerView.canScrollVertically(1)) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }


    private void loadMore() {

        rowsArrayList.add(null);
        noticeAdapter.notifyItemInserted(rowsArrayList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                noticeAdapter.notifyItemRemoved(scrollPosition);
                page++;
                setPostList(page);


            }
        }, 2000);


    }


    private void setPostList(int page){


        retrofit2.Call<List<NoticePost>> getNotices = api.getNoticePosts(page);

        getNotices.enqueue(new Callback<List<NoticePost>>() {
            @Override
            public void onResponse(Call<List<NoticePost>> call, Response<List<NoticePost>> response) {

                int index = rowsArrayList.size();

                if(response.isSuccessful()){

                    List<NoticePost> noticePostList = response.body();
                    if(noticePostList.size() == 0){
                        isLoading = false;
                        Toast.makeText(context, "No more posts!",Toast.LENGTH_SHORT).show();
                    }else {
                        for (NoticePost post : noticePostList) {
                            rowsArrayList.add(post);
                        }
                        noticeAdapter.notifyItemRangeInserted(index, rowsArrayList.size());
                        isLoading = false;
                        refreshLayout.setRefreshing(false);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<NoticePost>> call, Throwable t) {

            }
        });


    }

}