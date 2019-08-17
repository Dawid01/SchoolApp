package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

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
        ImageView imageView = view.findViewById(R.id.image);
        Glide.with(context).load(urls[position])
                .placeholder(R.mipmap.baseline_add_photo_alternate_white_36dp).centerCrop()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(imageView);
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
