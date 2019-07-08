package com.szczepaniak.dawid.appezn;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        new AccountDrawer(drawer, MainActivity.this);
        loadUser();
        loadPosts();
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

        postsLayout.removeAllViews();

        retrofit2.Call<User> userCall = api.getCurrentUser();


//        retrofit2.Call<PostList> postsCall = api.getAllPosts();
//
//        postsCall.enqueue(new Callback<PostList>() {
//            @Override
//            public void onResponse(Call<PostList> call, Response<PostList> response) {
//
//                if(response.isSuccessful()){
//
//                    List<Post> posts = response.body().getPostList();
//
//                    if(posts != null) {
//                        for (Post post : posts) {
//
//                            View postLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.post, null);
//                            TextView name = postLayout.findViewById(R.id.name);
//                            //name.setText(post.getUser().getName() + " " + post.getUser().getSurname());
//                            TextView status = postLayout.findViewById(R.id.status);
//
//                            switch (post.getPermission()) {
//
//                                case 0:
//                                    status.setText("Student");
//                                    break;
//                                case 1:
//                                    status.setText("Teacher");
//                                    break;
//                            }
//
//                            TextView date = postLayout.findViewById(R.id.date);
//                            date.setText(post.getDateTime());
//                            TextView content = postLayout.findViewById(R.id.content);
//                            content.setText(post.getContent());
//
//                            postsLayout.addView(postLayout);
//                        }
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PostList> call, Throwable t) {
//
//            }
//        });

        retrofit2.Call<Post> postsCall = api.getPost((long)(2));

        postsCall.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (response.isSuccessful()) {
                    final Post post = response.body();
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
                    postsLayout.addView(postLayout);

//                    retrofit2.Call<User> postUser = api.getUser(post.getUserID());
//
//                    postUser.enqueue(new Callback<User>() {
//                        @Override
//                        public void onResponse(Call<User> call, Response<User> response) {
//
//                            boolean b = response.isSuccessful();
//                            if(response.isSuccessful()){
//
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<User> call, Throwable t) {
//
//                        }
//                    });


                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

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
                    Picasso.get().load(user.getPhoto()).into(avatar);
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


}
