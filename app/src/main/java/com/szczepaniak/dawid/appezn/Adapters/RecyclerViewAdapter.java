package com.szczepaniak.dawid.appezn.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.szczepaniak.dawid.appezn.Activities.CommentsActivity;
import com.szczepaniak.dawid.appezn.Activities.PhotosViewerActivity;
import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.Assymetric.AsymmetricRecyclerView;
import com.szczepaniak.dawid.appezn.Assymetric.AsymmetricRecyclerViewAdapter;
import com.szczepaniak.dawid.appezn.Assymetric.Utils;
import com.szczepaniak.dawid.appezn.ItemImage;
import com.szczepaniak.dawid.appezn.Models.Comment;
import com.szczepaniak.dawid.appezn.Models.Post;
import com.szczepaniak.dawid.appezn.Models.PostReaction;
import com.szczepaniak.dawid.appezn.Models.User;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;
import com.szczepaniak.dawid.appezn.Singleton;
import com.szczepaniak.dawid.appezn.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    public List<Post> posts;


    public RecyclerViewAdapter(List<Post> posts, Context context) {
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


    @Override
    public long getItemId(int position) {
        return position;
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
        ImageView photo;
        AsymmetricRecyclerView photoAlbum;
        TextView likes;
        TextView dislikes;
        TextView comments;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
            content = itemView.findViewById(R.id.content);
            avatar = itemView.findViewById(R.id.avatar);
            likeIcon = itemView.findViewById(R.id.likeIcon);
            dislikeIcon = itemView.findViewById(R.id.dislikeIcon);
            commentIcon = itemView.findViewById(R.id.commentIcon);
            photo = itemView.findViewById(R.id.photo);
            photoAlbum = itemView.findViewById(R.id.photo_album);
            likes = itemView.findViewById(R.id.likes);
            dislikes = itemView.findViewById(R.id.dislikes);
            comments = itemView.findViewById(R.id.comments);

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

    private void populateItemRows(final ItemViewHolder viewHolder, int position) {

        final Post post = posts.get(position);
        final User user = post.getUser();
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

        viewHolder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().setPhotos(post.getPhotos());
                Singleton.getInstance().getMainActivity().startActivity(new Intent( Singleton.getInstance().getMainActivity(), PhotosViewerActivity.class));
            }
        });



        final List<Comment> comments = post.getComments();

        if(comments != null) {
            if (comments.size() == 0) {
                viewHolder.comments.setText(post.getComments().size() + " comment");
            } else {
                viewHolder.comments.setText(post.getComments().size() + " comments");
            }
        }

        viewHolder.commentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Singleton.getInstance().setComments(comments);
                Singleton.getInstance().setPost(post);
                Activity activity = Singleton.getInstance().getMainActivity();
                Intent commentsIntent = new Intent(activity, CommentsActivity.class);
                activity.startActivity(commentsIntent);

            }
        });


        int likes = 0;
        int dislikes = 0;

        final List<PostReaction> postReactions = post.getPostReactions();


        if(postReactions != null) {
            for (PostReaction reaction : postReactions) {

                if (reaction.getReaction() == 0) {
                    dislikes++;
                } else if (reaction.getReaction() == 1) {
                    likes++;
                }

                if(reaction.getUserID() == Singleton.getInstance().getCurrentUserID()){
                    int color = context.getResources().getColor(R.color.colorPrimary);
                    if (reaction.getReaction() == 0) {
                        viewHolder.dislikes.setTextColor(color);
                        viewHolder.dislikeIcon.setColorFilter(color);
                    } else if (reaction.getReaction() == 1) {
                        viewHolder.likes.setTextColor(color);
                        viewHolder.likeIcon.setColorFilter(color);
                    }
                }

            }
        }

        viewHolder.likes.setText(likes + " likes");
        viewHolder.dislikes.setText(dislikes + " dislikes");

        final ApiService api = RetroClient.getApiService();

        viewHolder.likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            PostReaction postReaction = new PostReaction(1, user.getId());
            retrofit2.Call<PostReaction> reactionCall = api.addReaction(postReaction, post.getId());

            reactionCall.enqueue(new Callback<PostReaction>() {
                @Override
                public void onResponse(Call<PostReaction> call, Response<PostReaction> response) {

                    if(response.isSuccessful()){

                        updatePostReactions(post, viewHolder);
                    }
                }

                @Override
                public void onFailure(Call<PostReaction> call, Throwable t) {
                    updatePostReactions(post, viewHolder);
                }
            });


            }
        });

        viewHolder.dislikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PostReaction postReaction = new PostReaction(0, user.getId());
                retrofit2.Call<PostReaction> reactionCall = api.addReaction(postReaction, post.getId());

                reactionCall.enqueue(new Callback<PostReaction>() {
                    @Override
                    public void onResponse(Call<PostReaction> call, Response<PostReaction> response) {

                        if(response.isSuccessful()){
                            updatePostReactions(post, viewHolder);
                       }
                    }

                    @Override
                    public void onFailure(Call<PostReaction> call, Throwable t) {
                        updatePostReactions(post, viewHolder);

                    }
                });

            }
        });


        AsymmetricRecyclerView album = viewHolder.photoAlbum;
        album.setItemViewCacheSize(500);
        album.setNestedScrollingEnabled(true);
        album.setRequestedColumnCount(3);
        album.setRequestedHorizontalSpacing(Utils.dpToPx(context, 1));
        album.addItemDecoration(new SpacesItemDecoration(context.getResources().getDimensionPixelSize(R.dimen.asymetric_grid_offset)));


        album.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        if(post.getPhotos() != null) {
            if (post.getPhotos().length == 1) {
                Glide.with(context).load(post.getPhotos()[0]).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).override(viewHolder.photo.getWidth()).into(viewHolder.photo);

            }else if(post.getPhotos().length > 1){

                List<ItemImage> postImages = new ArrayList<>();
                boolean isCol2Avail = false;

                for(int i = 0; i < post.getPhotos().length; i++){
                    String url = post.getPhotos()[i];
                    ItemImage postImage = new ItemImage(i,url, url);
                    int colSpan = 1;
                    if(i == 0){
                        colSpan = 2;
                    }

                    int rowSpan = colSpan;
                    if(colSpan == 2 && !isCol2Avail)
                        isCol2Avail = true;
                    else if(colSpan == 2 && isCol2Avail)
                        colSpan = 1;


                    if(post.getPhotos().length == 2){
                        colSpan = 3;
                        rowSpan = 2;

                    }else if(post.getPhotos().length == 4){

                        if(i == 0){
                            colSpan = 3;
                            rowSpan = 2;
                        }
                    }else if(post.getPhotos().length == 5){

                        if(i == 3){
                            colSpan = 2;
                            rowSpan = 1;
                        }
                    }

                    postImage.setColumnSpan(colSpan);
                    postImage.setRowSpan(rowSpan);
                    postImage.setPosition(i);
                    postImages.add(postImage);

                }

                List<ItemImage> photos = new ArrayList<>();
                String[] photoTable = new String[postImages.size()];

                for(int i = 0; i < postImages.size(); i++){

                    if(i <= 5) {
                        photos.add(postImages.get(i));
                    }
                    photoTable[i] = postImages.get(i).getImagePath();

                }


                ChildAdapter adapter = new ChildAdapter(photos,6, postImages.size(),photoTable);
                album.setAdapter(new AsymmetricRecyclerViewAdapter(context, album, adapter));


            }
        }

    }


    private void updatePostReactions(Post post, final ItemViewHolder viewHolder){

        ApiService api = RetroClient.getApiService();
        retrofit2.Call<Post> postCall = api.getPost(post.getId());

        postCall.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if(response.isSuccessful()){

                    int c = context.getResources().getColor(R.color.emoji_gray70);
                    viewHolder.dislikes.setTextColor(c);
                    viewHolder.dislikeIcon.setColorFilter(c);
                    viewHolder.likes.setTextColor(c);
                    viewHolder.likeIcon.setColorFilter(c);

                    int likes = 0;
                    int dislikes = 0;

                    Post post = response.body();

                    final List<PostReaction> postReactions = post.getPostReactions();


                    if(postReactions != null) {
                        for (PostReaction reaction : postReactions) {

                            if (reaction.getReaction() == 0) {
                                dislikes++;
                            } else if (reaction.getReaction() == 1) {
                                likes++;
                            }

                            if(reaction.getUserID() == Singleton.getInstance().getCurrentUserID()){
                                int color = context.getResources().getColor(R.color.colorPrimary);
                                if (reaction.getReaction() == 0) {
                                    viewHolder.dislikes.setTextColor(color);
                                    viewHolder.dislikeIcon.setColorFilter(color);
                                } else if (reaction.getReaction() == 1) {
                                    viewHolder.likes.setTextColor(color);
                                    viewHolder.likeIcon.setColorFilter(color);
                                }
                            }

                        }
                    }

                    viewHolder.likes.setText(likes + " likes");
                    viewHolder.dislikes.setText(dislikes + " dislikes");


                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });

    }



}
