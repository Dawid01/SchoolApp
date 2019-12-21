package com.szczepaniak.dawid.appezn.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.szczepaniak.dawid.appezn.Activities.CameraActivity;
import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.GalleryImage;
import com.szczepaniak.dawid.appezn.Models.Post;
import com.szczepaniak.dawid.appezn.PopUpGallery;
import com.szczepaniak.dawid.appezn.PostLoader;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;
import com.szczepaniak.dawid.appezn.Singleton;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostPage {

    private Context context;
    private View pageView;

    private EmojiEditText postEditText;
    private ImageView emojiBtm;
    private ImageView galleryBtm;
    private ImageView photoBtm;
    private ImageView sendBtm;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout selectedImages;
    private ImageView createPostAction;
    private ConstraintLayout createPostLayaout;
    private CardView createPostCard;
    private RecyclerView postsRecyclerView;
    private PostLoader postLoader;
    private ApiService api;
    private Singleton singleton;



    public PostPage(final Context context, View pageView) {
        this.context = context;
        this.pageView = pageView;

        postEditText = pageView.findViewById(R.id.post_edit_text);
        emojiBtm = pageView.findViewById(R.id.emojiBtm);
        emojiBtm.getDrawable().setColorFilter(Color.argb(255, 20, 177, 17), PorterDuff.Mode.MULTIPLY );
        galleryBtm = pageView.findViewById(R.id.gallerybtm);
        photoBtm = pageView.findViewById(R.id.photoBtm);
        selectedImages = pageView.findViewById(R.id.selected_images);
        refreshLayout = pageView.findViewById(R.id.Posts);
        sendBtm = pageView.findViewById(R.id.send);
        postsRecyclerView = pageView.findViewById(R.id.recicle_view_posts);
        createPostAction = pageView.findViewById(R.id.create_post_action);
        createPostLayaout = pageView.findViewById(R.id.create_post_layout);
        createPostCard = pageView.findViewById(R.id.create_post_card);
        api = RetroClient.getApiService();
        singleton = Singleton.getInstance();

        loadPosts();
        postCreatorLisnter();
        refreshPosts();
        loadEmoji();
        new PopUpGallery(galleryBtm, singleton.getMainActivity().findViewById(R.id.drawer_layout), singleton.getMainActivity(), selectedImages);


        createPostAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(createPostLayaout.getVisibility() == View.VISIBLE){
                    createPostLayaout.setVisibility(View.GONE);
                    createPostAction.setImageDrawable(context.getResources().getDrawable(R.mipmap.baseline_keyboard_arrow_down_black_24dp));
                }else {
                    createPostLayaout.setVisibility(View.VISIBLE);
                    createPostAction.setImageDrawable(context.getResources().getDrawable(R.mipmap.baseline_keyboard_arrow_up_black_24dp));

                }
            }
        });

        photoBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoActivity = new Intent(context, CameraActivity.class);
                context.startActivity(photoActivity);
                singleton.getMainActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.none );
            }
        });


    }

    void loadPosts(){

         postLoader = new PostLoader(postsRecyclerView, api, refreshLayout,context);

    }

    private void postCreatorLisnter(){

        sendBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MultipartBody.Part[] parts = new MultipartBody.Part[singleton.getGalleryImages().size()];
                final ArrayList<String> names = new ArrayList<>();
                String photosInfo = "";

                for(int i = 0; i < parts.length; i++){

                    File file = new File(singleton.getGalleryImages().get(i).getUrl());
                    RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                    names.add(file.getName());
                    MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), fileReqBody);
                    parts[i] = part;
                }

                if(parts.length != 0){
                    photosInfo = " with " + parts.length + " photo";
                    if(parts.length > 1){
                        photosInfo = photosInfo + "s";
                    }
                }

                retrofit2.Call<ResponseBody> uploadFilesCall = api.uploadFiles(parts);

                final ProgressDialog uploadDialog = ProgressDialog.show(context, "Loading...",
                        "Upload post"+ photosInfo, true);


                if(parts.length != 0) {
                    uploadFilesCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (response.isSuccessful()) {
                                String[] photos = new String[names.size()];

                                for (int i = 0; i < names.size(); i++) {

                                   //photos[i] = "http://192.168.0.110:8080/downloadFile/" + names.get(i);
                                   photos[i] = RetroClient.getRootUrl() + "downloadFile/" + names.get(i);
                                }

                                final Post newPost = new Post();
                                newPost.setContent(postEditText.getText().toString());
                                newPost.setPhotos(photos);

                                retrofit2.Call<Post> createPost = api.newPost(newPost);

                                createPost.enqueue(new Callback<Post>() {
                                    @Override
                                    public void onResponse(retrofit2.Call<Post> call, Response<Post> response) {

                                        uploadDialog.dismiss();
                                        createPostLayaout.setVisibility(View.GONE);
                                        LinearLayout photosLayout = createPostLayaout.findViewById(R.id.selected_images);
                                        photosLayout.removeAllViews();
                                        Singleton.getInstance().setGalleryImages(new ArrayList<GalleryImage>());

                                        if (response.isSuccessful()) {
                                            loadPosts();
                                            postEditText.setText("");
                                            singleton.setPhotos(new String[0]);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Post> call, Throwable t) {
                                        uploadDialog.dismiss();
                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            uploadDialog.dismiss();
                        }
                    });

                }else {

                    final Post newPost = new Post();
                    newPost.setContent(postEditText.getText().toString());

                    retrofit2.Call<Post> createPost = api.newPost(newPost);

                    createPost.enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(retrofit2.Call<Post> call, Response<Post> response) {

                            uploadDialog.dismiss();
                            createPostLayaout.setVisibility(View.GONE);
                            LinearLayout photosLayout = createPostLayaout.findViewById(R.id.selected_images);
                            photosLayout.removeAllViews();
                            Singleton.getInstance().setGalleryImages(new ArrayList<GalleryImage>());

                            if (response.isSuccessful()) {
                                loadPosts();
                                postEditText.setText("");
                                singleton.setPhotos(new String[0]);
                            }
                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {
                            uploadDialog.dismiss();
                        }
                    });
                }

                }
        });
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

                loadPosts();
            }
        });

    }

    private void loadEmoji(){

        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(singleton.getMainActivity().findViewById(R.id.drawer_layout)).build(postEditText);

        emojiBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(emojiPopup.isShowing()){
                    emojiPopup.dismiss();
                }else {
                    emojiPopup.toggle();
                }
            }
        });

    }
}
