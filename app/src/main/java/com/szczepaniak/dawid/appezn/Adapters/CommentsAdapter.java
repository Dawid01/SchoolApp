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
import com.szczepaniak.dawid.appezn.Singleton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<Comment> comments;
    private Context context;
    private ApiService api;
    private User currentUser;


    public CommentsAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
        api = RetroClient.getApiService();
        currentUser =  Singleton.getInstance().getCurrentUser();
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

//        if(comment.getUserID() == currentUser.getId()){
//            holder.name.setText(currentUser.getName() + " " + currentUser.getSurname());
//            Picasso.get().load(currentUser.getPhoto()).into(holder.avatar);
//        }

        retrofit2.Call<User> userCall = api.getUser(comment.getUserID());

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful()) {
                    User user = response.body();
                    holder.name.setText(user.getName() + " " + user.getSurname());
                    Picasso.get().load(user.getPhoto()).into(holder.avatar);
                }
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
            date = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
        }
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}