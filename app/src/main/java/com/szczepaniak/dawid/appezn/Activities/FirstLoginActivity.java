package com.szczepaniak.dawid.appezn.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.Models.User;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;
import com.szczepaniak.dawid.appezn.Singleton;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstLoginActivity extends AppCompatActivity {

    private Button change;

    private TextInputEditText newPasswordText;
    private TextInputEditText confirmPasswordText;

    private TextInputLayout newPasswordLayout;
    private TextInputLayout confirmPasswordLayout;

    private ApiService apiService;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        apiService = RetroClient.getApiService();
        change = findViewById(R.id.change_button);
        newPasswordText = findViewById(R.id.new_password_text);
        confirmPasswordText = findViewById(R.id.confirm_password_text);
        newPasswordLayout = findViewById(R.id.new_password);
        confirmPasswordLayout = findViewById(R.id.confirm_password);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean canChange = true;

                if(Objects.requireNonNull(newPasswordText.getText()).toString().equals("")){

                    newPasswordLayout.setError("Musisz wpisać nowe hasło!");
                    newPasswordLayout.setErrorEnabled(true);
                    canChange = false;
                }

                if(Objects.requireNonNull(confirmPasswordText.getText()).toString().equals("")){

                    confirmPasswordLayout.setError("Musisz potwierdzić nowe hasło!");
                    confirmPasswordLayout.setErrorEnabled(true);
                    canChange = false;
                }

                if(!confirmPasswordText.getText().toString().equals(newPasswordText.getText().toString())){
                    confirmPasswordLayout.setError("Hasła nie są takie same!");
                    confirmPasswordLayout.setErrorEnabled(true);
                    canChange = false;
                }

                if(canChange){

                    User user = Singleton.getInstance().getCurrentUser();
                    user.setPassword(newPasswordText.getText().toString());
                    Call<User> changePassword = apiService.changePassword(user.getId(), user);

                    pDialog = new ProgressDialog(FirstLoginActivity.this);
                    pDialog.setMessage("Zmienianie hasła...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();

                    changePassword.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {

                            if(response.isSuccessful()){

                                startActivity(new Intent(FirstLoginActivity.this, MainActivity.class));
                                Toast.makeText(FirstLoginActivity.this, "Hasło zostało zmienione", Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                                finish();
                            }else {
                                Toast.makeText(FirstLoginActivity.this, "Nie udało się zmienić hasła", Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(FirstLoginActivity.this, "Nie udało się zmienić hasła", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}
