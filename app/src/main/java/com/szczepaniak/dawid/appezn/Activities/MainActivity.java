package com.szczepaniak.dawid.appezn.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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
import com.szczepaniak.dawid.appezn.GalleryImage;
import com.szczepaniak.dawid.appezn.LessonPlanSystem;
import com.szczepaniak.dawid.appezn.Models.Post;
import com.szczepaniak.dawid.appezn.Models.User;
import com.szczepaniak.dawid.appezn.NoticeEzn.NoticeAdapter;
import com.szczepaniak.dawid.appezn.NoticeEzn.NoticeApiService;
import com.szczepaniak.dawid.appezn.NoticeEzn.NoticePost;
import com.szczepaniak.dawid.appezn.NoticeEzn.NoticePostList;
import com.szczepaniak.dawid.appezn.NoticeEzn.NoticeRetroClient;
import com.szczepaniak.dawid.appezn.PopUpGallery;
import com.szczepaniak.dawid.appezn.PostLoader;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;
import com.szczepaniak.dawid.appezn.Singleton;
import com.szczepaniak.dawid.appezn.ViewPager.PageAdapter;
import com.szczepaniak.dawid.appezn.ViewPager.ViewPage;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.twitter.TwitterEmojiProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private Spinner spinnerClass;
    private Spinner spinnerTypes;


   // private ConstraintLayout noticesLayout;

    private Singleton singleton;
    //private RecyclerView lessonsView;
    //private RecyclerView noticeRecyclerView;
    private LessonPlanSystem lessonPlanSystem;
   // private Spinner weekSpinner;
   // private ImageView next;
   // private ImageView back;
    private PostLoader postLoader;
    private NotificationManager mNotificationManager;
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private ViewPager pager;


    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        EmojiManager.install(new TwitterEmojiProvider());
        setContentView(R.layout.activity_main);
        api = RetroClient.getApiService();
        drawer = findViewById(R.id.nav_view);
        avatar = findViewById(R.id.avatar);
        bottonMenu = findViewById(R.id.bottomMenu);
        home = findViewById(R.id.Posts);
        plans = findViewById(R.id.Plans);
        notifications = findViewById(R.id.Notifications);
        drawerLayout = findViewById(R.id.drawer_layout);
        title = findViewById(R.id.Title);
        spinnerClass = findViewById(R.id.class_spinner);
        spinnerTypes = findViewById(R.id.type_spinner);


//        lessonsView = findViewById(R.id.lessons_view);
        TabLayout days = findViewById(R.id.week_days);
//        weekSpinner = findViewById(R.id.spinner_weeks);
//        noticesLayout = findViewById(R.id.notices_view);
//        next = findViewById(R.id.week_next);
//        back = findViewById(R.id.week_back);
       // lessonPlanSystem = new LessonPlanSystem(lessonsView, days, spinnerClass, spinnerTypes, next, back, weekSpinner, this);
        new AccountDrawer(drawer, MainActivity.this, this);

        singleton = Singleton.getInstance();
        singleton.setMainActivity(MainActivity.this);


        loadUser();

        pager = findViewById(R.id.pager);
        pager.setAdapter(new PageAdapter(MainActivity.this));
        buttonMenuListner();
        logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "chanel_id")
                        .setSmallIcon(R.mipmap.notification)
                        .setContentTitle("Ezn notification")
                        .setContentText("test1")
                        .setPriority(NotificationCompat.PRIORITY_LOW);



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "Test";
                    String description = "Description";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("chanel_id", name, importance);
                    channel.setDescription(description);
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                    notificationManager.notify(01, builder.build());

                }else {
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                    notificationManager.notify(01, builder.build());
                }

//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ezn.edu.pl/"));
//                startActivity(browserIntent);
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


        initImageLoader(MainActivity.this);
    }
        //new PopUpGallery(galleryBtm, drawerLayout, MainActivity.this, selectedImages);



    void loadNotices(){

        //noticeRecyclerView = findViewById(R.id.recicle_view_notices);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
       // noticeRecyclerView.setLayoutManager(layoutManager);
        NoticeApiService napi = NoticeRetroClient.getApiService();

        Call<List<NoticePost>> noticePostListCall = napi.getNoticePosts();

        noticePostListCall.enqueue(new Callback<List<NoticePost>>() {
            @Override
            public void onResponse(Call<List<NoticePost>> call, Response<List<NoticePost>> response) {

                if(response.isSuccessful()){

                    List<NoticePost> noticePostList = response.body();

                    NoticeAdapter noticeAdapter = new NoticeAdapter(noticePostList, MainActivity.this);
                    // recyclerView.setHasFixedSize(true);
                  //  noticeRecyclcontexterView.setItemViewCacheSize(500);
                  //  noticeRecyclerView.setNestedScrollingEnabled(false);
                   // noticeRecyclerView.setAdapter(noticeAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<NoticePost>> call, Throwable t) {

            }
        });

//        noticePostListCall.enqueue(new Callback<NoticePostList>() {
//            @Override
//            public void onResponse(Call<NoticePostList> call, Response<NoticePostList> response) {
//
//                if(response.isSuccessful()){
//
//                    NoticePostList noticePostList = response.body();
//
//                    NoticeAdapter noticeAdapter = new NoticeAdapter(noticePostList.getNoticePosts(), MainActivity.this);
//                    // recyclerView.setHasFixedSize(true);
//                    noticeRecyclerView.setItemViewCacheSize(500);
//                    noticeRecyclerView.setNestedScrollingEnabled(false);
//                    noticeRecyclerView.setAdapter(noticeAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NoticePostList> call, Throwable t) {
//
//            }
//        });


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
                        //createPostCard.setVisibility(View.GONE);
                    }else {
                        Picasso.get().load(user.getPhoto()).into(avatar);
                    }
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
                      //  noticesLayout.setVisibility(View.GONE);
                        notifications.setVisibility(View.GONE);
                       // createPostCard.setVisibility(View.VISIBLE);
                        title.setText("Home");
                        spinnerClass.setVisibility(View.GONE);
                        spinnerTypes.setVisibility(View.GONE);
                        title.setVisibility(View.VISIBLE);
                        title.setOnClickListener(null);
                        break;
                    case  R.id.navigation_plans:
                        home.setVisibility(View.GONE);
                        plans.setVisibility(View.VISIBLE);
                        //noticesLayout.setVisibility(View.GONE);
                        notifications.setVisibility(View.GONE);
                       // createPostCard.setVisibility(View.GONE);
                        spinnerClass.setVisibility(View.VISIBLE);
                        spinnerTypes.setVisibility(View.VISIBLE);
                        title.setVisibility(View.GONE);
                        break;
                    case R.id.navigation_notifications:
                        home.setVisibility(View.GONE);
                        plans.setVisibility(View.GONE);
                       // noticesLayout.setVisibility(View.GONE);
                        notifications.setVisibility(View.VISIBLE);
                        //createPostCard.setVisibility(View.GONE);
                        spinnerClass.setVisibility(View.GONE);
                        spinnerTypes.setVisibility(View.GONE);
                        title.setVisibility(View.VISIBLE);
                        title.setText("Notifications");
                        title.setOnClickListener(null);
                        break;
                    case R.id.notice:
                        home.setVisibility(View.GONE);
                        plans.setVisibility(View.GONE);
                       // noticesLayout.setVisibility(View.VISIBLE);
                        notifications.setVisibility(View.GONE);
                        //createPostCard.setVisibility(View.GONE);
                        spinnerClass.setVisibility(View.GONE);
                        spinnerTypes.setVisibility(View.GONE);
                        title.setVisibility(View.VISIBLE);
                        title.setText("Notices");
                        title.setOnClickListener(null);
                        loadNotices();
                        break;
                }

                return false;
            }
        });
    }


    public static void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();
        ImageLoader.getInstance().init(config.build());
    }

}




