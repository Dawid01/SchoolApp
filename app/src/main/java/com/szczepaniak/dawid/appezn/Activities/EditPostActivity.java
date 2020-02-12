package com.szczepaniak.dawid.appezn.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.GalleryImage;
import com.szczepaniak.dawid.appezn.Models.Post;
import com.szczepaniak.dawid.appezn.Models.User;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;
import com.szczepaniak.dawid.appezn.Singleton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class EditPostActivity extends AppCompatActivity {

    ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        api = RetroClient.getApiService();
        final Post post = Singleton.getInstance().getPost();
        final EditText postEdit = findViewById(R.id.post_edit_text);
        postEdit.setText(post.getContent());
        ImageView edit = findViewById(R.id.edit);
        final LinearLayout selectedGrid = findViewById(R.id.selected_images);

        for (final String galleryImage : post.getPhotos()) {

            ImageView img = new ImageView(this);
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            img.setLayoutParams(new GridView.LayoutParams(400, 400));
            Glide.with(this).load(galleryImage)
                    .placeholder(R.mipmap.baseline_add_photo_alternate_white_36dp).centerCrop()
                    .into(img);
            selectedGrid.addView(img);

            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            EditPostActivity.this);

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

//                        img.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                LayoutInflater inflater = LayoutInflater.from(context);
//                                View popUpView = inflater.inflate(R.layout.image_viewer, null);
//                                final PopupWindow popupWindow = new PopupWindow(popUpView, width, height, true);
//                                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
//
//                                final SubsamplingScaleImageView imageView = popUpView.findViewById(R.id.image);
//                                Glide.with(context)
//                                        .asBitmap()
//                                        .load(galleryImage)
//                                        .into(new SimpleTarget<Bitmap>() {
//                                            @Override
//                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                                imageView.setImage(ImageSource.bitmap(resource));
//
//                                            }
//                                        });
//                            }
//                        });

        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post.setContent(postEdit.getText().toString());
                Call<Post> postCall = api.putPost(post.getId(),post);

                postCall.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {

                        if(response.isSuccessful()){

                            Toast.makeText(EditPostActivity.this, "Post updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {

                    }
                });


            }
        });
    }
}
