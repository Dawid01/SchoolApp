package com.szczepaniak.dawid.appezn;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.szczepaniak.dawid.appezn.Models.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopUpGallery extends AppCompatActivity {


    private Activity activity;
    private GridView gallery;
    private static final int GALLERY = 1;
    private View parent;
    private ArrayList<GalleryImage> images;
    private int size;
    private List<GalleryImage> selectedImgs;
    private LinearLayout selectedGrid;
    private boolean isOneSelectGallery = false;
    private TextView nextButton;
    private ImageView avatar;
    private User user;
    private ApiService api;


    public PopUpGallery(View btmView,  final View parent,  final Activity activity) {

        this.activity = activity;
        this.parent = parent;
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point s = new Point();
        display. getSize(s);
        size = s.x/3;
        api = RetroClient.getApiService();

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

    public PopUpGallery(View btmView,  final View parent,  final Activity activity, LinearLayout selectedGrid) {

        this.activity = activity;
        this.parent = parent;
        this.selectedGrid = selectedGrid;
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


    private void createPopUp(final Context context, final View parent){
        selectedImgs = Singleton.getInstance().getGalleryImages();
        if(selectedImgs == null) {
            selectedImgs = new ArrayList<>();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View popUpView = inflater.inflate(R.layout.gallery_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popUpView, parent.getWidth(), parent.getHeight(), true);
        popupWindow.setAnimationStyle(R.style.galeryPopup);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        gallery = popUpView.findViewById(R.id.gallery);
        loadPhotos(activity);
        TextView close = popUpView.findViewById(R.id.close);
        TextView next = popUpView.findViewById(R.id.next);
        nextButton = next;

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });

        if(selectedGrid != null){

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!isOneSelectGallery) {
                        selectedGrid.removeAllViews();
                        for (final GalleryImage galleryImage : selectedImgs) {

                            ImageView img = new ImageView(activity);
                            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            img.setLayoutParams(new GridView.LayoutParams(200, 200));
                            Glide.with(activity).load(galleryImage.getUrl())
                                    .placeholder(R.mipmap.baseline_add_photo_alternate_white_36dp).centerCrop()
                                    .into(img);
                            selectedGrid.addView(img);

                            img.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(final View v) {

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            activity);

                                    alertDialogBuilder.setTitle("Delete?");
                                    alertDialogBuilder
                                            .setMessage("Click yes to delete!")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    List<GalleryImage> galleryImages = Singleton.getInstance().getGalleryImages();
                                                    galleryImages.remove(selectedGrid.indexOfChild(v));
                                                    Singleton.getInstance().setGalleryImages(galleryImages);
                                                    selectedGrid.removeView(v);
                                                }
                                            })

                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                    alertDialog.show();
                                    return false;
                                }
                            });

                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    LayoutInflater inflater = LayoutInflater.from(activity);
                                    View popUpView = inflater.inflate(R.layout.image_viewer, null);
                                    final PopupWindow popupWindow = new PopupWindow(popUpView, parent.getWidth(), parent.getHeight(), true);
                                    popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

                                    final SubsamplingScaleImageView imageView = popUpView.findViewById(R.id.image);
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(galleryImage.getUrl())
                                            .into(new SimpleTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    imageView.setImage(ImageSource.bitmap(resource));

                                                }
                                            });
                                }
                            });

                        }

                        Singleton.getInstance().setGalleryImages(selectedImgs);
                        popupWindow.dismiss();

                    }
                }
            });

        }else {
            next.setVisibility(View.GONE);
        }

    }

    private void loadPhotos(final Activity activity){

        //selectedImgs = Singleton.getInstance().getGalleryImages();
        gallery.setAdapter(new ImageAdapter(activity));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!isOneSelectGallery) {
                    if (selectedGrid != null) {
                        GalleryImage galleryImage = images.get(position);
                        if (!galleryImage.isUsed()) {
                            selectedImgs.add(galleryImage);
                            Blurry.with(activity).capture(view).into((ImageView) view);
                            galleryImage.setUsed(true);

                        } else {
                            selectedImgs.remove(selectedImgs.indexOf(galleryImage));
                            Glide.with(activity).load(galleryImage.getUrl())
                                    .placeholder(R.mipmap.baseline_add_photo_alternate_white_36dp).centerCrop()
                                    .into((ImageView) view);
                            galleryImage.setUsed(false);

                        }
                    }
                }else {

                    GalleryImage galleryImage = images.get(position);

                    File file = new File(galleryImage.getUrl());
                    RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                    final String name = file.getName();
                    MultipartBody.Part[] parts = new MultipartBody.Part[1];
                    MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), fileReqBody);
                    parts[0] = part;
                    Call<ResponseBody> uploadFilesCall = api.uploadFiles(parts);

                    uploadFilesCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if(response.isSuccessful()){

                                user.setPhoto(RetroClient.getRootUrl() + "downloadFile/" + name);
                                retrofit2.Call<User> userCall = api.putUser(user.getId(), user);
                                userCall.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        Picasso.get().load(response.body().getPhoto()).into(avatar);
                                        Toast.makeText(activity, "Change avatar", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });






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
            Collections.reverse(listOfAllImages);
            return listOfAllImages;
        }
    }

    public boolean isOneSelectGallery() {
        return isOneSelectGallery;
    }

    public void setOneSelectGallery(boolean oneSelectGallery, ImageView avatar, User user) {
        isOneSelectGallery = oneSelectGallery;
        this.avatar = avatar;
        this.user = user;
    }

    public TextView getNextButton() {
        return nextButton;
    }
}
