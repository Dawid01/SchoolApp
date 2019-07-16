package com.szczepaniak.dawid.appezn;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private NavigationView drawer;
    private ImageView logo;
    private ImageView avatar;
    private ApiService api;
    private LinearLayout postsLayout;
    private BottomNavigationView bottonMenu;
    private View home;
    private View plans;
    private View notifications;
    private DrawerLayout drawerLayout;
    private TextView title;
    private EmojIconActions emojIconActions;
    private EmojiconEditText postEditText;
    private ImageView emojiBtm;
    private ImageView galleryBtm;
    private ImageView photoBtm;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout selectedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = RetroClient.getApiService();
        drawer = findViewById(R.id.nav_view);
        postsLayout = findViewById(R.id.PostsLayout);
        avatar = findViewById(R.id.avatar);
        bottonMenu = findViewById(R.id.bottomMenu);
        home = findViewById(R.id.Posts);
        plans = findViewById(R.id.Plans);
        notifications = findViewById(R.id.Notifications);
        drawerLayout = findViewById(R.id.drawer_layout);
        title = findViewById(R.id.Title);
        postEditText = findViewById(R.id.post_edit_text);
        emojiBtm = findViewById(R.id.emojiBtm);
        emojiBtm.getDrawable().setColorFilter(Color.argb(255, 20, 177, 17), PorterDuff.Mode.MULTIPLY );
        galleryBtm = findViewById(R.id.gallerybtm);
        photoBtm = findViewById(R.id.photoBtm);
        selectedImages = findViewById(R.id.selected_images);
        refreshLayout = findViewById(R.id.Posts);


        new AccountDrawer(drawer, MainActivity.this);
        loadUser();
        loadPosts();
        loadEmoji();
        refreshPosts();
        new PopUpGallery(galleryBtm, drawerLayout, MainActivity.this, selectedImages);
        new PhotoPopUp(photoBtm, drawerLayout, MainActivity.this);
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
    }

    void loadPosts(){


        int postLayoutSize = postsLayout.getChildCount() - 1;

        for (int i = postLayoutSize; i > 0; i--) {

            postsLayout.removeViewAt(i);

        }


        retrofit2.Call<PostList> allPosts = api.getAllPosts("id,desc");

        allPosts.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {

                if(response.isSuccessful()){

                    List<Post> posts = response.body().getPostList();

                    for (final Post post : posts){

                        retrofit2.Call<User> postUser = api.getUser(post.getUserID());

                        postUser.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {

                                if(response.isSuccessful()){

                                    final View postLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.post, null);
                                    final TextView name = postLayout.findViewById(R.id.name);
                                    final ImageView avatar = postLayout.findViewById(R.id.avatar);

                                    TextView status = postLayout.findViewById(R.id.status);

                                    switch (post.getPermission()) {

                                        case 0:
                                            status.setText("Student");
                                            break;
                                        case 1:
                                            status.setText("Teacher");
                                            break;
                                    }

                                    TextView date = postLayout.findViewById(R.id.date);
                                    date.setText(post.getDateTime());
                                    TextView content = postLayout.findViewById(R.id.content);
                                    content.setText(post.getContent());
                                    name.setText(response.body().getName() + " " + response.body().getSurname());
                                    //Picasso.get().load(response.body().getPhoto()).into(avatar);
                                    Glide.with(MainActivity.this).load(response.body().getPhoto()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(avatar);
                                    postsLayout.addView(postLayout);
                                    refreshLayout.setRefreshing(false);

                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                refreshLayout.setRefreshing(false);

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {

            }
        });

    }

    private void loadUser() {

        retrofit2.Call<User> userCall = api.getCurrentUser();
        //retrofit2.Call<User> userCall = api.getUser(singleton.getCurrentUserID());


        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    final User user = response.body();
                    Glide.with(MainActivity.this).load(user.getPhoto()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(avatar);
                    //Picasso.get().load(user.getPhoto()).into(avatar);
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
                        title.setText("Home");
                        break;
                    case  R.id.navigation_plans:
                        home.setVisibility(View.GONE);
                        plans.setVisibility(View.VISIBLE);
                        notifications.setVisibility(View.GONE);
                        title.setText("Plans");
                        break;
                    case R.id.navigation_notifications:
                        home.setVisibility(View.GONE);
                        plans.setVisibility(View.GONE);
                        notifications.setVisibility(View.VISIBLE);
                        title.setText("Notifications");
                        break;
                }

                return false;
            }
        });
    }

    private void loadEmoji(){

        emojIconActions = new EmojIconActions(this, drawerLayout, postEditText, emojiBtm);
        emojIconActions.ShowEmojIcon();

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


}




