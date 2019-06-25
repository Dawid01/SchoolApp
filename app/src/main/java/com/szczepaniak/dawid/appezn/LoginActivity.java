package com.szczepaniak.dawid.appezn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailInputLayout;
    private TextInputEditText emailText;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText passwordText;
    private Button loginButton;
    ApiService apiService;
    private ProgressDialog pDialog;
    private Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInputLayout = findViewById(R.id.email_layout);
        emailText = findViewById(R.id.email_text);
        passwordInputLayout = findViewById(R.id.password_layout);
        passwordText = findViewById(R.id.password_text);
        loginButton = findViewById(R.id.login_button);
        apiService = RetroClient.getApiService();
        singleton = Singleton.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });
    }

    void login() {

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        emailInputLayout.setErrorEnabled(false);
        passwordInputLayout.setErrorEnabled(false);


        if (email.equals("")) {
            emailInputLayout.setError("You need to enter a email");
        } else if (password.equals("")) {
            passwordInputLayout.setError("You need to enter a password");

        } else {

            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            String base = email + ":" + password;
            String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
            Call<User> userCall = apiService.loginUser(authHeader);

            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    pDialog.dismiss();
                    if (response.isSuccessful()) {
                        singleton.setCurrentUser(response.body());
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e("login", "ERROR", t);
                    Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
