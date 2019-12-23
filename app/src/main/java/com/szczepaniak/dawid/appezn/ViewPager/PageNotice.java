package com.szczepaniak.dawid.appezn.ViewPager;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szczepaniak.dawid.appezn.NoticeEzn.NoticeLoader;
import com.szczepaniak.dawid.appezn.NoticeEzn.NoticeRetroClient;
import com.szczepaniak.dawid.appezn.R;


public class PageNotice {

    private Context context;
    private View pageView;
    private RecyclerView noticeRecyclerView;
    private SwipeRefreshLayout refreshLayout;

    public PageNotice(Context context, View pageView) {
        this.context = context;
        this.pageView = pageView;
        noticeRecyclerView = pageView.findViewById(R.id.recicle_view_notices);
        refreshLayout = pageView.findViewById(R.id.notice_refresh);
        loadNotices();
        refreshPosts();
    }

    private void refreshPosts(){

        refreshLayout.setColorSchemeColors(
                context.getResources().getColor(R.color.colorPrimary),
                context.getResources().getColor(R.color.colorPrimaryDark),
                context.getResources().getColor(R.color.colorAccent),
                context.getResources().getColor(R.color.colorPrimaryDark));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadNotices();
            }
        });

    }

    void  loadNotices(){

        new NoticeLoader(noticeRecyclerView, NoticeRetroClient.getApiService(), refreshLayout, context);

    }

//    void loadNotices(){
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
//        noticeRecyclerView.setLayoutManager(layoutManager);
//        NoticeApiService napi = NoticeRetroClient.getApiService();
//
//        Call<List<NoticePost>> noticePostListCall = napi.getNoticePosts();
//
//        noticePostListCall.enqueue(new Callback<List<NoticePost>>() {
//            @Override
//            public void onResponse(Call<List<NoticePost>> call, Response<List<NoticePost>> response) {
//
//                if(response.isSuccessful()){
//
//                    List<NoticePost> noticePostList = response.body();
//
//                     NoticeAdapter noticeAdapter = new NoticeAdapter(noticePostList, context);
//                     noticeRecyclerView.setNestedScrollingEnabled(false);
//                     noticeRecyclerView.setAdapter(noticeAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<NoticePost>> call, Throwable t) {
//
//            }
//        });

//        noticePostListCall.enqueue(new Callback<NoticePostList>() {
//            @Override
//            public void onResponse(Call<NoticePostList> call, Response<NoticePostList> response) {
//
//                if(response.isSuccessful()){
//
//                    NoticePostList noticePostList = response.body();
//
//                    NoticeAdapter noticeAdapter = new NoticeAdapter(noticePostList.getNoticePosts(), MainActivity.this);
//                    // recyclerView.setHasFixedSize(true);
//                    noticeRecyclerView.setItemViewCacheSize(500);
//                    noticeRecyclerView.setNestedScrollingEnabled(false);
//                    noticeRecyclerView.setAdapter(noticeAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NoticePostList> call, Throwable t) {
//
//            }
//        });


   // }

}
