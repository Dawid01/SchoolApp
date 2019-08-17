package com.szczepaniak.dawid.appezn;

import android.content.Context;
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
import com.szczepaniak.dawid.appezn.Assymetric.AsymmetricRecyclerView;
import com.szczepaniak.dawid.appezn.Assymetric.AsymmetricRecyclerViewAdapter;
import com.szczepaniak.dawid.appezn.Assymetric.Utils;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    public List<Post> posts;
    private View parent;

    public RecyclerViewAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    public RecyclerViewAdapter(List<Post> posts, Context context, View parent) {
        this.posts = posts;
        this.context = context;
        this.parent = parent;
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
        ImageView photo;
        AsymmetricRecyclerView photoAlbum;

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
            photo = itemView.findViewById(R.id.photo);
            photoAlbum = itemView.findViewById(R.id.photo_album);

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

        final Post post = posts.get(position);
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

        AsymmetricRecyclerView album = viewHolder.photoAlbum;
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

                Glide.with(context).load(post.getPhotos()[0]).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(viewHolder.photo);

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
                        photoTable[i] = postImages.get(i).getImagePath();
                    }

//                    if(i == 5){
//                        break;
//                    }
                }


                ChildAdapter adapter = new ChildAdapter(photos,6, postImages.size());
                album.setAdapter(new AsymmetricRecyclerViewAdapter(context, album, adapter));


            }
        }

    }





}
