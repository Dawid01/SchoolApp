package com.szczepaniak.dawid.appezn.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.szczepaniak.dawid.appezn.GalleryImage;
import com.szczepaniak.dawid.appezn.PhotoView;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.Singleton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private PhotoView photoView;
    private ImageView takePhoto;
    private ImageView back;
    private ImageView switchCamera;
    private ImageView ok;
    private static final int CAMERA = 1;
    private boolean isPhotoTaken = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        hideSystemUI();

        photoView = findViewById(R.id.photoView);
        takePhoto = findViewById(R.id.take_photo);
        back = findViewById(R.id.back);
        switchCamera = findViewById(R.id.switch_camera);
        ok = findViewById(R.id.ok);

        takePhoto.setTranslationY(200f);
        takePhoto.animate()
                .translationY(0f)
                .alpha(1f);

        switchCamera.setTranslationY(200f);
        switchCamera.animate()
                .translationY(0f)
                .alpha(1f);


        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.CAMERA}, CAMERA);
            } else {
                photoView.startCamera();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSystemUI();
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoView.stopCamera();
                isPhotoTaken = true;
                takePhoto.animate()
                        .translationY(200)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                takePhoto.setVisibility(View.GONE);
                            }
                        });

                switchCamera.animate()
                        .translationY(200)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                takePhoto.setVisibility(View.GONE);
                                ok.setVisibility(View.VISIBLE);
                                ok.setTranslationY(200f);
                                ok.animate()
                                        .translationY(0f)
                                        .alpha(1f);
                            }
                        });

            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveTempBitmap(photoView.getBitmap());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPhotoTaken) {
                    photoView.stopCamera();
                    finish();
                }else {
                    startActivity(new Intent(CameraActivity.this, CameraActivity.class));
                    finish();
                }
            }
        });

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoView.switchCamera();
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.none, R.anim.slide_out_down);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
            hideSystemUI();
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    photoView.startCamera();
                } else {
                    finish();
                }
                break;
        }
    }


    public void saveTempBitmap(Bitmap bitmap) {
        if (isExternalStorageWritable()) {
            saveImage(bitmap);
        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "EZN_photo_"+ timeStamp +".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, new String[]{file.getName()}, null);
            List<GalleryImage> galleryImages  = Singleton.getInstance().getGalleryImages();
            galleryImages.add(new GalleryImage(Uri.fromFile(file).toString(), true));
            Singleton.getInstance().setGalleryImages(galleryImages);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
