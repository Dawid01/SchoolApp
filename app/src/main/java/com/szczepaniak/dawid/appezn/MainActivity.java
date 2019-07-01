package com.szczepaniak.dawid.appezn;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private NavigationView drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.nav_view);
        new AccountDrawer(drawer);
    }




}
