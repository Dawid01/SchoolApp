package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.szczepaniak.dawid.appezn.Activities.BinaryGame;
import com.szczepaniak.dawid.appezn.Activities.LoginActivity;
import com.szczepaniak.dawid.appezn.Activities.MainActivity;
import com.szczepaniak.dawid.appezn.Activities.SettingsActivity;
import com.szczepaniak.dawid.appezn.Activities.UserActivity;
import com.szczepaniak.dawid.appezn.Models.User;

import retrofit2.Callback;
import retrofit2.Response;

public class AccountDrawer {

    private NavigationView drawer;
    private ApiService api;
    private TextView info;
    private TextView email;
    private ImageView avatar;
    private Singleton singleton;
    private Context context;
    private AppCompatActivity appCompatActivity;
    private ImageView banner;

    public AccountDrawer(NavigationView drawer, final Context context, AppCompatActivity appCompatActivity) {
        this.drawer = drawer;
        this.context = context;
        this.appCompatActivity = appCompatActivity;
        View header = drawer.getHeaderView(0);
        info = header.findViewById(R.id.info);
        avatar = header.findViewById(R.id.avatar);
        email = header.findViewById(R.id.email);
        banner = header.findViewById(R.id.banner);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            Drawable drawable = context.getResources().getDrawable(R.drawable.banner_black);
            banner.setBackground(drawable);
        }
        api = RetroClient.getApiService();
        singleton = Singleton.getInstance();
        final Context c = context;
        final AppCompatActivity a = appCompatActivity;
        loadUser();
        drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch(id){
                    case R.id.Account:
                        Intent account = new Intent(c, UserActivity.class);
                        c.startActivity(account);
                        break;
                    case R.id.Games:
                        Intent binaryGame = new Intent(c, BinaryGame.class);
                        c.startActivity(binaryGame);
                        break;
                    case  R.id.Settings:
                        c.startActivity(new Intent(c, SettingsActivity.class));
                        break;
                    case R.id.logout:
                        Methods.clearData(c);
                        c.startActivity(new Intent(c, LoginActivity.class));
                        a.finish();
                        break;
                }
                return false;
            }
        });

    }


    private void loadUser() {

        retrofit2.Call<User> userCall = api.getCurrentUser();

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    final User user = response.body();
                    info.setText(user.getName() + " " + user.getSurname());
                    email.setText(user.getEmail());
                    //Glide.with(context).load(user.getPhoto()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(avatar);
                    Picasso.get().load(user.getPhoto()).into(avatar);

                    if(user.getEmail().equals("guest@ezn.pl")){
                        info.setVisibility(View.GONE);
                        email.setVisibility(View.GONE);
                        avatar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                Log.e("flash", "ERROR", t);
            }
        });
    }

    public boolean onNavigationItemSelected(MenuItem item) {


        return true;
    }
}