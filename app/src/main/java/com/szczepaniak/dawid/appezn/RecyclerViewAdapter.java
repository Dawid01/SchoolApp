package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    public List<Post> posts;


    public RecyclerViewAdapter(List<Post> posts, Context context ) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return posts.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView date;
        TextView status;
        TextView content;
        ImageView avatar;
        ImageView likeIcon;
        ImageView dislikeIcon;
        ImageView commentIcon;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            content = itemView.findViewById(R.id.content);
            avatar = itemView.findViewById(R.id.avatar);
            likeIcon = itemView.findViewById(R.id.likeIcon);
            dislikeIcon = itemView.findViewById(R.id.dislikeIcon);
            commentIcon = itemView.findViewById(R.id.commentIcon);

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        Post post = posts.get(position);
        User user = post.getUser();
        if(user != null) {
            viewHolder.name.setText(user.getName() + " " + user.getSurname());
            Picasso.get().load(user.getPhoto()).into(viewHolder.avatar);
            switch (post.getPermission()) {
                case 0:
                    viewHolder.status.setText("Student");
                    break;
                case 1:
                    viewHolder.status.setText("Teacher");
                    break;
            }
        }
        viewHolder.content.setText(post.getContent());
        viewHolder.date.setText(post.getDateTime());
        viewHolder.likeIcon.setColorFilter(context.getResources().getColor(R.color.emoji_gray70));
        viewHolder.dislikeIcon.setColorFilter(context.getResources().getColor(R.color.emoji_gray70));
        viewHolder.commentIcon.setColorFilter(context.getResources().getColor(R.color.emoji_gray70));

    }


}
