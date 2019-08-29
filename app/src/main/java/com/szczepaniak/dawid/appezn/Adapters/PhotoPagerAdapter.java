package com.szczepaniak.dawid.appezn.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.szczepaniak.dawid.appezn.R;

public class PhotoPagerAdapter extends PagerAdapter {

    private Context context;
    private String[] urls;

    public PhotoPagerAdapter(Context context, String[] urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {


        View view = LayoutInflater.from(context).inflate(R.layout.image_viewer, null);
        final SubsamplingScaleImageView imageView = view.findViewById(R.id.image);

        Glide.with(context)
                .asBitmap()
                .load(urls[position])
                .into(new SimpleTarget<Bitmap>() {
                          @Override
                          public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                              imageView.setImage(ImageSource.bitmap(resource));

                          }
                      });

        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(position);
    }

}
