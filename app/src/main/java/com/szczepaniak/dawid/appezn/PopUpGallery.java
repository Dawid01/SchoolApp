package com.szczepaniak.dawid.appezn;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import jp.wasabeef.blurry.Blurry;

public class PopUpGallery extends AppCompatActivity {


    private Activity activity;
    private GridView gallery;
    private static final int GALLERY = 1;
    private View parent;
    private ArrayList<GalleryImage> images;
    private int size;
    private ArrayList<Integer> selectedImgs;




    public PopUpGallery(View btmView,  final View parent,  final Activity activity) {

        this.activity = activity;
        this.parent = parent;
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point s = new Point();
        display. getSize(s);
        size = s.x/3;

        btmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY);
                    } else {
                        createPopUp(activity, parent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void createPopUp(Context context, View parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View popUpView = inflater.inflate(R.layout.gallery_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popUpView, parent.getWidth(), parent.getHeight(), true);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        popupWindow.setAnimationStyle(R.style.galeryPopup);
        gallery = popUpView.findViewById(R.id.gallery);
        loadPhotos(activity);
        TextView close = popUpView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });
    }

    private void loadPhotos(final Activity activity){

        selectedImgs = new ArrayList<>();
        gallery.setAdapter(new ImageAdapter(activity));

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GalleryImage galleryImage = images.get(position);
                if(!galleryImage.isUsed()){
                    Blurry.with(activity).capture(view).into((ImageView) view);
                    galleryImage.setUsed(true);

                }else {

                    Glide.with(activity).load(galleryImage.getUrl())
                            .placeholder(R.mipmap.baseline_add_photo_alternate_white_36dp).centerCrop()
                            .into((ImageView)view);
                    galleryImage.setUsed(false);

                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createPopUp(activity, parent);

                } else {
                }
                break;
        }
    }


    private class ImageAdapter extends BaseAdapter {

        /** The context. */
        private Activity context;

        public ImageAdapter(Activity localContext) {
            context = localContext;
            images = getAllShownImagesPath(context);
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(size, size));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load(images.get(position).getUrl())
                    .placeholder(R.mipmap.baseline_add_photo_alternate_white_36dp).centerCrop()
                    .into(picturesView);

            if(Arrays.asList(selectedImgs).contains((int)position)){
                Blurry.with(activity).capture(picturesView).into(picturesView);
            }

            if(images.get(position).isUsed()) {
                try {
                    Blurry.with(activity).capture(picturesView).into(picturesView);
                }catch (NullPointerException e){}
            }

            return picturesView;
        }


        private ArrayList<GalleryImage> getAllShownImagesPath(Activity activity) {
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            ArrayList<GalleryImage> listOfAllImages = new ArrayList<GalleryImage>();
            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                GalleryImage galleryImage = new GalleryImage(absolutePathOfImage, false);

                listOfAllImages.add(galleryImage);
            }
            return listOfAllImages;
        }
    }
}
