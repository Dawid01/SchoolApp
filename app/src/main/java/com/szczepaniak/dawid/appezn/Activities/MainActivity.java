package com.szczepaniak.dawid.appezn.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.szczepaniak.dawid.appezn.AccountDrawer;
import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.CommentUpSlider;
import com.szczepaniak.dawid.appezn.Models.User;
import com.szczepaniak.dawid.appezn.NoticeEzn.NoticeAdapter;
import com.szczepaniak.dawid.appezn.NoticeEzn.NoticeApiService;
import com.szczepaniak.dawid.appezn.NoticeEzn.NoticePost;
import com.szczepaniak.dawid.appezn.NoticeEzn.NoticeRetroClient;
import com.szczepaniak.dawid.appezn.PostLoader;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;
import com.szczepaniak.dawid.appezn.Singleton;
import com.szczepaniak.dawid.appezn.ViewPager.PageAdapter;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.twitter.TwitterEmojiProvider;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements InternetConnectivityListener{

    private NavigationView drawer;
    private ImageView logo;
    private ImageView avatar;
    private ApiService api;
    private BottomNavigationView bottonMenu;
    private DrawerLayout drawerLayout;
    private TextView title;
    private Spinner spinnerClass;
    private Spinner spinnerTypes;
    private Singleton singleton;
    private ViewPager pager;
    private CommentUpSlider commentUpSlider;
    private ConstraintLayout networkConection;
    private InternetAvailabilityChecker internetAvailabilityChecker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        singleton = Singleton.getInstance();
        singleton.setMainActivity(MainActivity.this);

        EmojiManager.install(new TwitterEmojiProvider());
        setContentView(R.layout.activity_main);
        api = RetroClient.getApiService();
        drawer = findViewById(R.id.nav_view);
        avatar = findViewById(R.id.avatar);
        bottonMenu = findViewById(R.id.bottomMenu);
        drawerLayout = findViewById(R.id.drawer_layout);
        title = findViewById(R.id.title);
        spinnerClass = findViewById(R.id.class_spinner);
        singleton.setSpinnerClass(spinnerClass);
        spinnerTypes = findViewById(R.id.type_spinner);
        singleton.setSpinnerTypes(spinnerTypes);
        networkConection = findViewById(R.id.network_comunicator);
        new AccountDrawer(drawer, MainActivity.this, this);
        loadUser();

        InternetAvailabilityChecker.init(this);
        internetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        internetAvailabilityChecker.addInternetConnectivityListener(this);
//
//        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        boolean isMetered = cm.isActiveNetworkMetered();
//


        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(new PageAdapter(MainActivity.this));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                switch (i){
                    case 0:
                        changeToPosts();
                        bottonMenu.setSelectedItemId(R.id.navigation_home);
                        break;
                    case 1:
                        changeToNotice();
                        bottonMenu.setSelectedItemId(R.id.notice);
                        break;
                    case 2:
                        changeToPlans();
                        bottonMenu.setSelectedItemId(R.id.navigation_plans);
                        break;
                    case 3:
                        changeToNotifications();
                        bottonMenu.setSelectedItemId(R.id.navigation_notifications);
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

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

        commentUpSlider = new CommentUpSlider(this);

    }


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if(isConnected){

            networkConection.setVisibility(View.GONE);
        }else {

            networkConection.setVisibility(View.VISIBLE);
        }
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
                        Picasso.get().load(user.getPhoto()).placeholder(R.drawable.avatar).into(avatar);
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
                        changeToPosts();
                        pager.setCurrentItem(0);
                        break;
                    case R.id.notice:
                        changeToNotice();
                        pager.setCurrentItem(1);
                        break;
                    case  R.id.navigation_plans:
                        changeToPlans();
                        pager.setCurrentItem(2);
                        break;
                    case R.id.navigation_notifications:
                        changeToNotifications();
                        pager.setCurrentItem(3);
                        break;

                }

                return false;
            }
        });
    }


    void changeToPosts(){
        title.setText("Home");
        spinnerClass.setVisibility(View.GONE);
        spinnerTypes.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        title.setOnClickListener(null);
    }

    void changeToNotice(){
        spinnerClass.setVisibility(View.GONE);
        spinnerTypes.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        title.setText("Ogłoszenia");
        title.setOnClickListener(null);
        pager.setCurrentItem(1);
    }

    void changeToPlans(){
        spinnerClass.setVisibility(View.VISIBLE);
        spinnerTypes.setVisibility(View.VISIBLE);
        title.setVisibility(View.GONE);
        pager.setCurrentItem(2);
    }

    void changeToNotifications(){
        spinnerClass.setVisibility(View.GONE);
        spinnerTypes.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        title.setText("Powiadomienia");
        title.setOnClickListener(null);
        pager.setCurrentItem(3);
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


    @Override
    public void onBackPressed() {

        SlidingUpPanelLayout sliding = findViewById(R.id.sliding_layout);
        if(sliding.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN){
            sliding.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            bottonMenu.setVisibility(View.VISIBLE);
        }

    }

    public CommentUpSlider getCommentUpSlider() {
        return commentUpSlider;
    }
}




