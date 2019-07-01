package com.szczepaniak.dawid.appezn;

import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountDrawer {

    private NavigationView drawer;
    private ApiService api;
    TextView info;
    private Singleton singleton;

    public AccountDrawer(NavigationView drawer) {
        this.drawer = drawer;
        View header = drawer.getHeaderView(0);
        info = header.findViewById(R.id.info);
        api = RetroClient.getApiService();
        singleton = Singleton.getInstance();
        loadUser();
    }


    private void loadUser() {

        retrofit2.Call<User> userCall = api.getCurrentUser();
        //retrofit2.Call<User> userCall = api.getUser(singleton.getCurrentUserID());


        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    final User user = response.body();
                    info.setText(user.getName() + " " + user.getSurname());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                Log.e("flash", "ERROR", t);
            }
        });
    }
}