package com.szczepaniak.dawid.appezn.NoticeEzn;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.szczepaniak.dawid.appezn.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class ImageGetter implements Html.ImageGetter {

    private Context c;
    private TextView container;
    private UrlImageDownloader urlDrawable;
    private ArrayList<String> images = new ArrayList<>();


    public ImageGetter(View textView, Context context) {
        this.c = context;
        this.container = (TextView) textView;
    }

    @Override
    public Drawable getDrawable(String source) {
        urlDrawable = new UrlImageDownloader(c.getResources(), source);
        urlDrawable.drawable = c.getResources().getDrawable(R.drawable.avatar);

        ImageLoader.getInstance().loadImage(source, new SimpleListener(urlDrawable));
        return urlDrawable;
    }

    private class SimpleListener extends SimpleImageLoadingListener {
        UrlImageDownloader urlImageDownloader;

        public SimpleListener(UrlImageDownloader downloader) {
            super();
            urlImageDownloader = downloader;
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            images.add(imageUri);
//            int width = loadedImage.getWidth();
//            int height = loadedImage.getHeight();
//
//            int newWidth = width;
//            int newHeight = height;
//
//            if (width > container.getWidth()) {
//                newWidth = container.getWidth();
//                newHeight = (newWidth * height) / width;
//            }
//
//            if (view != null) {
//                view.getLayoutParams().width = newWidth;
//                view.getLayoutParams().height = newHeight;
//            }
//
//            Drawable result = new BitmapDrawable(c.getResources(), loadedImage);
//            result.setBounds(0, 0, newWidth, newHeight);
//
//            urlImageDownloader.setBounds(1, 1, newWidth, newHeight);
//            urlImageDownloader.drawable = result;
//            container.setHeight((container.getHeight() + result.getIntrinsicHeight()));
//            container.invalidate();
        }
    }

    private class UrlImageDownloader extends BitmapDrawable {
        public Drawable drawable;

        public UrlImageDownloader(Resources res, String filepath) {
            super(res, filepath);
            drawable = new BitmapDrawable(res, filepath);
        }

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }


}