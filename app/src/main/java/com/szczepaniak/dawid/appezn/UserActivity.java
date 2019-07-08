package com.szczepaniak.dawid.appezn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private ApiService api;
    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        api = RetroClient.getApiService();
        avatar = findViewById(R.id.avatar);

        retrofit2.Call<User> userCall = api.getCurrentUser();

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful()){

                    Picasso.get().load(response.body().getPhoto()).into(avatar);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


    }
}
