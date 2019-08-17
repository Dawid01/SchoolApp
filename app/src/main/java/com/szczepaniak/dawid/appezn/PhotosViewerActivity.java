package com.szczepaniak.dawid.appezn;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class PhotosViewerActivity extends AppCompatActivity {


    private Singleton singleton;
    private ViewPager viewPager;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photos_viewer);
        singleton = Singleton.getInstance();
        viewPager = findViewById(R.id.photos);
        info = findViewById(R.id.info);
        info.setText(1 + "/" + singleton.getPhotos().length);

        viewPager.setAdapter(new PhotoPagerAdapter(this, singleton.getPhotos()));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                info.setText(i + 1 + "/" + singleton.getPhotos().length);

            }

            @Override
            public void onPageSelected(int i) {
                info.setText(i + 1 + "/" + singleton.getPhotos().length);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

                //info.setText(i + 1 + "/" + singleton.getPhotos().length);

            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View mDecorView = this.getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
