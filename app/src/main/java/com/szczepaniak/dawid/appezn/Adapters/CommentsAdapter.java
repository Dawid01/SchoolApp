package com.szczepaniak.dawid.appezn.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.Models.Comment;
import com.szczepaniak.dawid.appezn.Models.User;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<Comment> comments;
    private Context context;
    private ApiService api;

    public CommentsAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
        api = RetroClient.getApiService();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.content.setText(comment.getContent());
        holder.date.setText(comment.getDateTime());

        retrofit2.Call<User> userCall = api.getUser(comment.getUserID());

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User user = response.body();
                holder.name.setText(user.getName() + " " + user.getSurname());
                Picasso.get().load(user.getPhoto()).into(holder.avatar);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


    }

    @Override public int getItemCount() {
        return comments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatar;
        public TextView content;
        public TextView date;
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
        }
    }
}