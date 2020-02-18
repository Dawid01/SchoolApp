package com.szczepaniak.dawid.appezn.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.GalleryImage;
import com.szczepaniak.dawid.appezn.Models.User;
import com.szczepaniak.dawid.appezn.PopUpGallery;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private ApiService api;
    private ImageView avatar;
    private View rootView;
    private User user;
    private PopUpGallery popUpGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        api = RetroClient.getApiService();
        avatar = findViewById(R.id.avatar);
        rootView = findViewById(R.id.root_view);

        retrofit2.Call<User> userCall = api.getCurrentUser();

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful()){
                    user = response.body();
                    Picasso.get().load(response.body().getPhoto()).placeholder(R.drawable.avatar).into(avatar);
                    popUpGallery = new PopUpGallery(avatar, rootView,UserActivity.this);
                    popUpGallery.setOneSelectGallery(true, avatar, user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });




    }
}
