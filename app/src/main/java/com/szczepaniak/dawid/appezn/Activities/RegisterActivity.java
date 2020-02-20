package com.szczepaniak.dawid.appezn.Activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.Models.User;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextView back;
    private TextInputEditText email;
    private TextInputEditText name;
    private TextInputEditText surname;
    private TextInputEditText className;
    private Button create;
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        back = findViewById(R.id.back);
        email = findViewById(R.id.email_text);
        name = findViewById(R.id.name_text);
        surname = findViewById(R.id.surname_text);
        className = findViewById(R.id.class_text);
        create = findViewById(R.id.register_button);
        api = RetroClient.getApiService();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User();
                user.setName(name.getText().toString());
                user.setSurname(surname.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPermissions(0);
                Call<String> createAccount = api.createAccount(user, className.getText().toString());

                createAccount.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {


                        Toast.makeText(RegisterActivity.this, response.body(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, ":(", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}
