package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.szczepaniak.dawid.appezn.Adapters.RecyclerViewAdapter;
import com.szczepaniak.dawid.appezn.Models.Post;
import com.szczepaniak.dawid.appezn.Models.PostList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLoader {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private int page = 0;
    private ArrayList<Post> rowsArrayList;
    private boolean isLoading;
    private ApiService api;
    private Context context;
    private SwipeRefreshLayout refreshLayout;

    public PostLoader(RecyclerView recyclerView, ApiService api, SwipeRefreshLayout refreshLayout, Context context) {
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

        recyclerViewAdapter = new RecyclerViewAdapter(rowsArrayList, context);
        recyclerViewAdapter.setHasStableIds(true);
       // recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(recyclerViewAdapter);
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

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
//                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
//                        loadMore();
//                        isLoading = true;
//                    }

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
        recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);
       // recyclerViewAdapter.notifyDataSetChanged();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                page++;
                setPostList(page);


            }
        }, 2000);


    }


    private void setPostList(int page){
        retrofit2.Call<PostList> allPosts = api.getAllPosts(page, 10,"id,desc");


        allPosts.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {

                if(response.isSuccessful()){

                    ArrayList<Post> posts = (ArrayList<Post>) response.body().getPostList();
                    if(posts.size() == 0){
                        isLoading = false;
                       // Toast.makeText(context, "No more posts!",Toast.LENGTH_SHORT).show();
                    }else {
                        for (Post post : posts) {
                            rowsArrayList.add(post);
                        }
                        recyclerViewAdapter.notifyDataSetChanged();
                        isLoading = false;
                        refreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;

            }
        });

    }

    public RecyclerViewAdapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }
}
