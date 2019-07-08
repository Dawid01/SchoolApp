package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    public AccountDrawer(NavigationView drawer, Context context) {
        this.drawer = drawer;
        this.context = context;
        View header = drawer.getHeaderView(0);
        info = header.findViewById(R.id.info);
        avatar = header.findViewById(R.id.avatar);
        email = header.findViewById(R.id.email);
        api = RetroClient.getApiService();
        singleton = Singleton.getInstance();
        final Context c = context;
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
                    Picasso.get().load(user.getPhoto()).into(avatar);
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