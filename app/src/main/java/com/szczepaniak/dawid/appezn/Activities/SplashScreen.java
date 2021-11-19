package com.szczepaniak.dawid.appezn.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.Methods;
import com.szczepaniak.dawid.appezn.Models.User;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;
import com.szczepaniak.dawid.appezn.Singleton;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {


    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private ApiService apiService;
    private Singleton singleton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = this.getSharedPreferences(
                "Settings", Context.MODE_PRIVATE);
        if(prefs.getBoolean("DarkTheme", false)){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        apiService = RetroClient.getApiService();
        singleton = Singleton.getInstance();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                tryLogin();

            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void tryLogin(){

        HashSet<String> cookies = Methods.getCookies(SplashScreen.this);
        if(cookies.size() != 0){

            SharedPreferences sharedPreferences = SplashScreen.this.getSharedPreferences("email",Context.MODE_PRIVATE);
            login(Objects.requireNonNull(sharedPreferences.getString("email", "null")), "null");
        }else {
            goToLoginActivity();
        }
    }


    private void login(String email, String password){

        String base = email + ":" + password;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<User> userCall = apiService.loginUser(authHeader);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                goToLoginActivity();

            }
        },  2 * SPLASH_DISPLAY_LENGTH);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if (response.isSuccessful()) {
                    SharedPreferences sharedPreferences = SplashScreen.this.getSharedPreferences("email", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email).apply();


                    Call<User> currentUser = apiService.getCurrentUser();

                    currentUser.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {

                            if(response.isSuccessful()){
                                assert response.body() != null;
                                singleton.setCurrentUser(response.body());
                                if(response.body().isPassworChanged()) {
                                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }else {
                                    Intent mainIntent = new Intent(SplashScreen.this, FirstLoginActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            }else {
                                Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            goToLoginActivity();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                goToLoginActivity();
            }
        });
    }

    private void goToLoginActivity(){
        Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }
}