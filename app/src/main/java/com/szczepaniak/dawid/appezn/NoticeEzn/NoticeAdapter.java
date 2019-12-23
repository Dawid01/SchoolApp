package com.szczepaniak.dawid.appezn.NoticeEzn;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.szczepaniak.dawid.appezn.Adapters.RecyclerViewAdapter;
import com.szczepaniak.dawid.appezn.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.http.PUT;

public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<NoticePost> posts;
    private Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    public NoticeAdapter(List<NoticePost> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_post, parent, false);
            return new NoticeAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new NoticeAdapter.LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof NoticeAdapter.ViewHolder) {
            loadNoticePost((NoticeAdapter.ViewHolder)holder, position);
        }else if (holder instanceof NoticeAdapter.LoadingViewHolder) {
            showLoadingView((NoticeAdapter.LoadingViewHolder) holder, position);
        }

    }


    void loadNoticePost(NoticeAdapter.ViewHolder holder, int position){

        NoticePost post = posts.get(position);
        holder.name.setText(post.getTitle().getRendered());
        DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMAN);
        try {
            Date date = inputFormat.parse(post.getDate());
            String out = outputFormat.format(date);
            holder.time.setText(out);
        }catch (ParseException e){
            holder.time.setText(post.getDate());
        }
        holder.content.setText(Html.fromHtml(post.getContent().getRendered()));
        holder.content.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showLoadingView(NoticeAdapter.LoadingViewHolder holder, int position) {

    }

    @Override public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return posts.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
         public TextView time;
         public TextView name;
         public TextView content;


        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

}