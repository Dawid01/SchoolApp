package com.szczepaniak.dawid.appezn.Activities;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

    private TextInputLayout emailLayout;
    private TextInputLayout nameLayout;
    private TextInputLayout surnameLayout;
    private TextInputLayout classNameLayout;
    private CheckBox privacyPolicy;
    private String privacyPolicyURL = "http://ezn.edu.pl/";
    private ProgressDialog pDialog;

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
        emailLayout = findViewById(R.id.email);
        nameLayout = findViewById(R.id.name);
        surnameLayout = findViewById(R.id.surname);
        classNameLayout = findViewById(R.id.class_input);
        privacyPolicy = findViewById(R.id.privacy_policy);
        privacyPolicy.setText(Html.fromHtml("Rejestrując się potwierdzasz nasze zasady <a color=\"#14b1ae\"; href= " + privacyPolicyURL + ">polityki prywatności </a>"));
        api = RetroClient.getApiService();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                nameLayout.setErrorEnabled(false);
            }
        });

        surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                surnameLayout.setErrorEnabled(false);
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                emailLayout.setErrorEnabled(false);
            }
        });

        className.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                classNameLayout.setErrorEnabled(false);
            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean canSend = true;

                if(name.getText().toString().equals("")){

                    nameLayout.setError("Musisz wpisac imię!");
                    nameLayout.setErrorEnabled(true);
                    canSend = false;
                }

                if(surname.getText().toString().equals("")){

                    surnameLayout.setError("Musisz wpisać nazwisko!");
                    surnameLayout.setErrorEnabled(true);
                    canSend = false;
                }


                if(email.getText().toString().equals("")){

                    emailLayout.setError("Musisz wpisać email!");
                    emailLayout.setErrorEnabled(true);
                    canSend = false;
                }


                if(className.getText().toString().equals("")){

                    classNameLayout.setError("Musisz wpisać nazwe klasy!");
                    classNameLayout.setErrorEnabled(true);
                    canSend = false;
                }



                if(canSend) {

                    if (privacyPolicy.isChecked()) {
                        User user = new User();
                        user.setName(name.getText().toString());
                        user.setSurname(surname.getText().toString());
                        user.setEmail(email.getText().toString());
                        user.setPermissions(0);
                        Call<String> createAccount = api.createAccount(user, className.getText().toString());

                        pDialog = new ProgressDialog(RegisterActivity.this);
                        pDialog.setMessage("Wysyłanie prośby o założenie konta...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();

                        createAccount.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {


                                Toast.makeText(RegisterActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(RegisterActivity.this, ":(", Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();

                            }
                        });
                    }else {
                        Toast.makeText(RegisterActivity.this, "Musisz zaakceptować politykę prywatności", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
