package com.szczepaniak.dawid.appezn.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.szczepaniak.dawid.appezn.Activities.CommentsActivity;
import com.szczepaniak.dawid.appezn.Activities.EditPostActivity;
import com.szczepaniak.dawid.appezn.Activities.MainActivity;
import com.szczepaniak.dawid.appezn.Activities.PhotosViewerActivity;
import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.Assymetric.AsymmetricRecyclerView;
import com.szczepaniak.dawid.appezn.Assymetric.AsymmetricRecyclerViewAdapter;
import com.szczepaniak.dawid.appezn.Assymetric.Utils;
import com.szczepaniak.dawid.appezn.GalleryImage;
import com.szczepaniak.dawid.appezn.ItemImage;
import com.szczepaniak.dawid.appezn.Models.Comment;
import com.szczepaniak.dawid.appezn.Models.Post;
import com.szczepaniak.dawid.appezn.Models.PostReaction;
import com.szczepaniak.dawid.appezn.Models.User;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;
import com.szczepaniak.dawid.appezn.Singleton;
import com.szczepaniak.dawid.appezn.SpacesItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private Context context;
    public List<Post> posts;
    long currentUserID;


    public RecyclerViewAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
        currentUserID = Singleton.getInstance().getCurrentUserID();

    }


    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(view);

            viewHolder.setIsRecyclable(true);
            viewHolder.likeIcon.setColorFilter(context.getResources().getColor(R.color.emoji_gray70));
            viewHolder.dislikeIcon.setColorFilter(context.getResources().getColor(R.color.emoji_gray70));
            viewHolder.commentIcon.setColorFilter(context.getResources().getColor(R.color.emoji_gray70));

            AsymmetricRecyclerView album = viewHolder.photoAlbum;
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

            return viewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        long startTime = System.currentTimeMillis();
        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
        Log.i("LAG", "bindView time: " + (System.currentTimeMillis() - startTime));

    }


    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_TYPE_LOADING = 1;
        return posts.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

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
        ImageView edit;

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
            edit = itemView.findViewById(R.id.edit_button);

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

    }

    @SuppressLint("SetTextI18n")
    private void populateItemRows(final ItemViewHolder viewHolder, int position) {

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
        if(post.getContent().equals("")){
            viewHolder.content.setTextSize(0);
        }
        viewHolder.date.setText(post.getDateTime());

        assert user != null;
        if(user.getId() == currentUserID){

            viewHolder.edit.setVisibility(View.VISIBLE);
            viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Singleton.getInstance().setPost(post);
                    context.startActivity(new Intent(context, EditPostActivity.class));
                }
            });
        }

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
                MainActivity mainActivity = (MainActivity) Singleton.getInstance().getMainActivity();
                mainActivity.getCommentUpSlider().openPanel();

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
                likeReaction(viewHolder, user, api, post);
            }
        });

        viewHolder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeReaction(viewHolder, user, api, post);
            }
        });

        viewHolder.dislikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dislikeReaction(viewHolder, user, api, post);
            }
        });

        viewHolder.dislikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dislikeReaction(viewHolder, user, api, post);
            }
        });


        AsymmetricRecyclerView album = viewHolder.photoAlbum;
        List<ItemImage> postImages = new ArrayList<>();
        if(post.getPhotos() != null) {
            for (int i = 0; i < post.getPhotos().length; i++) {
                String url = post.getPhotos()[i];
                ItemImage postImage = new ItemImage(i, url, url);
                int colSpan = 1;
                if (i == 0) {
                    colSpan = 2;
                }

                int rowSpan = colSpan;

                if (post.getPhotos().length == 1) {
                    colSpan = 3;
                    rowSpan = 3;
                }

                if (post.getPhotos().length == 2) {
                    colSpan = 3;
                    rowSpan = 2;

                } else if (post.getPhotos().length == 4) {

                    if (i == 0) {
                        colSpan = 3;
                        rowSpan = 2;
                    }
                } else if (post.getPhotos().length == 5) {

                    if (i == 3) {
                        colSpan = 2;
                        rowSpan = 1;
                    }
                }

                postImage.setColumnSpan(colSpan);
                postImage.setRowSpan(rowSpan);
                postImage.setPosition(i);
                postImages.add(postImage);

            }
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
        adapter.setHasStableIds(true);
        album.setHasFixedSize(true);
        album.setNestedScrollingEnabled(false);
        album.setItemViewCacheSize(500);
        album.setDrawingCacheEnabled(true);
        album.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        album.setAdapter(new AsymmetricRecyclerViewAdapter(context, album, adapter));

    }


    private void  likeReaction(ItemViewHolder viewHolder, User user, ApiService api, Post post){

        PostReaction postReaction = new PostReaction(1, user.getId());
        retrofit2.Call<PostReaction> reactionCall = api.addReaction(postReaction, post.getId());
        int iconColor = context.getResources().getColor(R.color.emoji_gray70);
        int textColor = context.getResources().getColor(R.color.emoji_white);
        int color = context.getResources().getColor(R.color.colorPrimary);

        if(viewHolder.likes.getCurrentTextColor() == textColor){
            viewHolder.likes.setTextColor(color);
            viewHolder.likeIcon.setColorFilter(color);
        }else {
            viewHolder.likes.setTextColor(textColor);
            viewHolder.likeIcon.setColorFilter(iconColor);
        }

        reactionCall.enqueue(new Callback<PostReaction>() {
            @Override
            public void onResponse(Call<PostReaction> call, Response<PostReaction> response) {
                updatePostReactions(post, viewHolder);

            }

            @Override
            public void onFailure(Call<PostReaction> call, Throwable t) {
                updatePostReactions(post, viewHolder);
            }
        });
    }


    private void  dislikeReaction(ItemViewHolder viewHolder, User user, ApiService api, Post post){

        PostReaction postReaction = new PostReaction(0, user.getId());
        retrofit2.Call<PostReaction> reactionCall = api.addReaction(postReaction, post.getId());
        int iconColor = context.getResources().getColor(R.color.emoji_gray70);
        int textColor = context.getResources().getColor(R.color.emoji_white);
        int color = context.getResources().getColor(R.color.colorPrimary);

        if(viewHolder.dislikes.getCurrentTextColor() == textColor){
            viewHolder.dislikes.setTextColor(color);
            viewHolder.dislikeIcon.setColorFilter(color);
        }else {
            viewHolder.dislikes.setTextColor(textColor);
            viewHolder.dislikeIcon.setColorFilter(iconColor);
        }

        reactionCall.enqueue(new Callback<PostReaction>() {
            @Override
            public void onResponse(Call<PostReaction> call, Response<PostReaction> response) {

                updatePostReactions(post, viewHolder);

            }

            @Override
            public void onFailure(Call<PostReaction> call, Throwable t) {
                updatePostReactions(post, viewHolder);

            }
        });
    }



        private void updatePostReactions(Post post, final ItemViewHolder viewHolder){

        Log.i("REACTIONS", "update reaction");

        ApiService api = RetroClient.getApiService();
        retrofit2.Call<Post> postCall = api.getPost(post.getId());

        postCall.enqueue(new Callback<Post>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if(response.isSuccessful()){

                    int iconColor = context.getResources().getColor(R.color.emoji_gray70);
                    int textColor = context.getResources().getColor(R.color.emoji_white);
                    viewHolder.dislikes.setTextColor(textColor);
                    viewHolder.dislikeIcon.setColorFilter(iconColor);
                    viewHolder.likes.setTextColor(textColor);
                    viewHolder.likeIcon.setColorFilter(iconColor);

                    int likes = 0;
                    int dislikes = 0;

                    Post post = response.body();

                    assert post != null;
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
            public void onFailure(@NotNull Call<Post> call, @NotNull Throwable t) {

            }
        });

    }



}
