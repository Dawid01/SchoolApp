package com.szczepaniak.dawid.appezn.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.picasso.Picasso;
import com.szczepaniak.dawid.appezn.AccountDrawer;
import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.LessonPlanSystem;
import com.szczepaniak.dawid.appezn.Models.Post;
import com.szczepaniak.dawid.appezn.Models.User;
import com.szczepaniak.dawid.appezn.PopUpGallery;
import com.szczepaniak.dawid.appezn.PostLoader;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;
import com.szczepaniak.dawid.appezn.Singleton;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.twitter.TwitterEmojiProvider;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class MainActivity extends AppCompatActivity {

    private NavigationView drawer;
    private ImageView logo;
    private ImageView avatar;
    private ApiService api;
    private BottomNavigationView bottonMenu;
    private View home;
    private View plans;
    private View notifications;
    private DrawerLayout drawerLayout;
    private TextView title;
    private Spinner spinner;
    private EmojiEditText postEditText;
    private ImageView emojiBtm;
    private ImageView galleryBtm;
    private ImageView photoBtm;
    private ImageView sendBtm;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout selectedImages;
    private RecyclerView recyclerView;
    private ImageView createPostAction;
    private ConstraintLayout createPostLayaout;
    private CardView createPostCard;
    private Singleton singleton;
    private RecyclerView lessonsView;
    private LessonPlanSystem lessonPlanSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EmojiManager.install(new TwitterEmojiProvider());
        setContentView(R.layout.activity_main);
        api = RetroClient.getApiService();
        drawer = findViewById(R.id.nav_view);
        // = findViewById(R.id.PostsLayout);
        avatar = findViewById(R.id.avatar);
        bottonMenu = findViewById(R.id.bottomMenu);
        home = findViewById(R.id.Posts);
        plans = findViewById(R.id.Plans);
        notifications = findViewById(R.id.Notifications);
        drawerLayout = findViewById(R.id.drawer_layout);
        title = findViewById(R.id.Title);
        spinner = findViewById(R.id.class_spinner);
        postEditText = findViewById(R.id.post_edit_text);
        emojiBtm = findViewById(R.id.emojiBtm);
        emojiBtm.getDrawable().setColorFilter(Color.argb(255, 20, 177, 17), PorterDuff.Mode.MULTIPLY );
        galleryBtm = findViewById(R.id.gallerybtm);
        photoBtm = findViewById(R.id.photoBtm);
        selectedImages = findViewById(R.id.selected_images);
        refreshLayout = findViewById(R.id.Posts);
        sendBtm = findViewById(R.id.send);
        recyclerView = findViewById(R.id.recicle_view_posts);
        createPostAction = findViewById(R.id.create_post_action);
        createPostLayaout = findViewById(R.id.create_post_layout);
        createPostCard = findViewById(R.id.create_post_card);
        lessonsView = findViewById(R.id.lessons_view);
        TabLayout days = findViewById(R.id.week_days);
        lessonPlanSystem = new LessonPlanSystem(lessonsView, days, spinner,this);
        new AccountDrawer(drawer, MainActivity.this);

        singleton = Singleton.getInstance();
        singleton.setMainActivity(MainActivity.this);


        loadUser();
        loadPosts();
        loadEmoji();
        postCreatorLisnter();
        refreshPosts();
        new PopUpGallery(galleryBtm, drawerLayout, MainActivity.this, selectedImages);
        buttonMenuListner();
        logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ezn.edu.pl/"));
                startActivity(browserIntent);
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(drawerLayout.isDrawerOpen(drawer)){
                    drawerLayout.closeDrawers();
                }else {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        });

        createPostAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(createPostLayaout.getVisibility() == View.VISIBLE){
                    createPostLayaout.setVisibility(View.GONE);
                    createPostAction.setImageDrawable(MainActivity.this.getResources().getDrawable(R.mipmap.baseline_keyboard_arrow_down_black_24dp));
                }else {
                    createPostLayaout.setVisibility(View.VISIBLE);
                    createPostAction.setImageDrawable(MainActivity.this.getResources().getDrawable(R.mipmap.baseline_keyboard_arrow_up_black_24dp));

                }
            }
        });

        photoBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoActivity = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(photoActivity);
                overridePendingTransition( R.anim.slide_in_up, R.anim.none );
            }
        });

        initImageLoader(MainActivity.this);
    }

    void loadPosts(){

        new PostLoader(recyclerView, api, refreshLayout,MainActivity.this);

    }

    private void postCreatorLisnter(){

        sendBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Post newPost = new Post();
                newPost.setContent(postEditText.getText().toString());

                File file = new File(singleton.getGalleryImages().get(0).getUrl());
                Uri uri = Uri.fromFile(file);

                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse(getContentResolver().getType(uri)),
                                file
                        );

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), requestFile);


                    retrofit2.Call<Post> createPost = api.newPost(newPost);

                    createPost.enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {

                            if (response.isSuccessful()) {

                                loadPosts();
                                postEditText.setText("");
                                singleton.setPhotos(new String[0]);
                            }
                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {

                        }
                    });
                }


//                    retrofit2.Call<ResponseBody> uploadFile = api.uploadFile(body);
//
//                    uploadFile.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                            if(response.isSuccessful()){
//
//                                Post newPost = new Post();
//                                newPost.setContent(postEditText.getText().toString());
//                                String[] photos = new String[1];
//                                photos[0] = response.body().toString();
//                                newPost.setPhotos(photos);
//
//                                retrofit2.Call<Post> createPost = api.newPost(newPost);
//
//                                createPost.enqueue(new Callback<Post>() {
//                                    @Override
//                                    public void onResponse(Call<Post> call, Response<Post> response) {
//
//                                        if (response.isSuccessful()) {
//
//                                            loadPosts();
//                                            postEditText.setText("");
//                                            singleton.setPhotos(new String[0]);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<Post> call, Throwable t) {
//
//                                    }
//                                });
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                        }
//                    });
//                }


        });
    }


    private void loadUser() {

        retrofit2.Call<User> userCall = api.getCurrentUser();


        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    final User user = response.body();
                    Singleton.getInstance().setCurrentUserID(user.getId());
                    if(user.getEmail().equals("guest@ezn.pl")){
                        createPostCard.setVisibility(View.GONE);
                    }else {
                        Picasso.get().load(user.getPhoto()).into(avatar);
                    }
                    //Glide.with(MainActivity.this).load(user.getPhoto()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(avatar);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                Log.e("flash", "ERROR", t);
            }
        });
    }

    private void buttonMenuListner(){

        bottonMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();
                menuItem.setChecked(true);
                switch (id){

                    case R.id.navigation_home:
                        home.setVisibility(View.VISIBLE);
                        plans.setVisibility(View.GONE);
                        notifications.setVisibility(View.GONE);
                        createPostCard.setVisibility(View.VISIBLE);
                        title.setText("Home");
                        spinner.setVisibility(View.GONE);
                        title.setVisibility(View.VISIBLE);
                        title.setOnClickListener(null);
                        break;
                    case  R.id.navigation_plans:
                        home.setVisibility(View.GONE);
                        plans.setVisibility(View.VISIBLE);
                        notifications.setVisibility(View.GONE);
                        createPostCard.setVisibility(View.GONE);
                        //title.setText("Plans");
                        spinner.setVisibility(View.VISIBLE);
                        title.setVisibility(View.GONE);
                        lessonPlanSystem.loadClasses();
                        break;
                    case R.id.navigation_notifications:
                        home.setVisibility(View.GONE);
                        plans.setVisibility(View.GONE);
                        notifications.setVisibility(View.VISIBLE);
                        createPostCard.setVisibility(View.GONE);
                        spinner.setVisibility(View.GONE);
                        title.setVisibility(View.VISIBLE);
                        title.setText("Notifications");
                        title.setOnClickListener(null);
                        break;
                }

                return false;
            }
        });
    }

    private void loadEmoji(){

        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(drawerLayout).build(postEditText);

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

    private void refreshPosts(){

        refreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadPosts();
            }
        });

    }

    public static void initImageLoader(Context context) {

        Singleton singleton = Singleton.getInstance();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

}




