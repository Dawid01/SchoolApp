package com.szczepaniak.dawid.appezn.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailInputLayout;
    private TextInputEditText emailText;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText passwordText;
    private TextView guest;
    private TextView signin;
    private Button loginButton;
    ApiService apiService;
    private ProgressDialog pDialog;
    private Singleton singleton;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiService = RetroClient.getApiService();
        emailInputLayout = findViewById(R.id.email);
        emailText = findViewById(R.id.email_text);
        passwordInputLayout = findViewById(R.id.password);
        passwordText = findViewById(R.id.password_text);
        loginButton = findViewById(R.id.login_button);
        singleton = Singleton.getInstance();
        guest = findViewById(R.id.guest);
        signin = findViewById(R.id.signin);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        HashSet<String> cookies = Methods.getCookies(LoginActivity.this);
        if(cookies.size() != 0){

            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("email",Context.MODE_PRIVATE);
            login(Objects.requireNonNull(sharedPreferences.getString("email", "null")), "null", true);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailText.getText().toString().toLowerCase();
                String password = passwordText.getText().toString();
                login(email, password, true);
            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login("guest@ezn.pl", "guest", false);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    void login(final String email, String password, final boolean save) {

        emailInputLayout.setErrorEnabled(false);
        passwordInputLayout.setErrorEnabled(false);
        if (email.equals("")) {
            emailInputLayout.setError("Musisz wpisać email!");
        } else if (password.equals("")) {
            passwordInputLayout.setError("Musisz wpisać hasło!");

        } else {

            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logowanie...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            String base = email + ":" + password;
            String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
            Call<User> userCall = apiService.loginUser(authHeader);

            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                    pDialog.dismiss();
                    if (response.isSuccessful()) {
                        if(save) {
                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("email", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email).apply();
                        }

                        Call<User> currentUser = apiService.getCurrentUser();

                        currentUser.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {

                                if(response.isSuccessful()){
                                    assert response.body() != null;
                                    singleton.setCurrentUser(response.body());
                                    if(response.body().isPassworChanged()) {
                                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(mainIntent);
                                        finish();
                                    }else {
                                        Intent mainIntent = new Intent(LoginActivity.this, FirstLoginActivity.class);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });

                    }else {

                        switch (response.code()){
                            case 401:
                                Toast.makeText(LoginActivity.this, "Email lub hasło jest niepoprawne", Toast.LENGTH_LONG).show();
                                break;
                            case 404:
                                Toast.makeText(LoginActivity.this, "404 error", Toast.LENGTH_LONG).show();
                                break;
                            case 500:
                                Toast.makeText(LoginActivity.this, "Server nie odpowiada :(", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(LoginActivity.this, "Nieznany błąd", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e("login", "ERROR", t);
                    Toast.makeText(LoginActivity.this, "Brak połaczenia", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    public static boolean hasPermissions(Context context, String... permissions)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null)
        {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
    }
}
