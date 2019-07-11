package com.szczepaniak.dawid.appezn;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

public class PhotoPopUp extends AppCompatActivity {

    private Activity activity;
    private View parent;
    private PhotoView photoView;
    private static final int PHOTO = 1;
    private Camera camera;


    public PhotoPopUp(View btmView, View parent, Camera camera, final Activity activity) {
        this.activity = activity;
        this.parent = parent;

       btmView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
//               try {
//                   if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                       ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PHOTO);
//                   } else {
//                       createPopUp(activity);
//                   }
//               } catch (Exception e) {
//                   e.printStackTrace();
//               }
               createPopUp(activity);
           }
       });
    }

    private void createPopUp(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        final View popUpView = inflater.inflate(R.layout.camera_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popUpView, parent.getWidth(), parent.getHeight(), true);
        ConstraintLayout root = popUpView.findViewById(R.id.root);

        PhotoView photoView = new PhotoView(context, camera);
        root.addView(photoView);

        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        View takePhoto = popUpView.findViewById(R.id.take_photo);
        View switchCamera = popUpView.findViewById(R.id.switch_camera);
        View back = popUpView.findViewById(R.id.back);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //take photo
            }
        });

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //switch camera
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createPopUp(activity);
                } else {
                }
                break;
        }
    }

}
