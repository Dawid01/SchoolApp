package com.szczepaniak.dawid.appezn.NoticeEzn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szczepaniak.dawid.appezn.R;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{

private List<NoticePost> posts;
private Context context;


public NoticeAdapter(List<NoticePost> posts, Context context) {
        this.posts = posts;
        this.context = context;

        }

    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_post, parent, false);
        return new NoticeAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NoticeAdapter.ViewHolder holder, int position) {
        NoticePost post = posts.get(position);
        holder.name.setText(post.getTitle().getRendered());
        holder.time.setText(post.getDate());
        holder.content.setText(Html.fromHtml(post.getContent().getRendered()));
    }

    @Override public int getItemCount() {
        return posts.size();
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

}