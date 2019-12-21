package com.szczepaniak.dawid.appezn.NoticeEzn;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;

import com.szczepaniak.dawid.appezn.Activities.MyApplication;

public class ImageGetter implements Html.ImageGetter {

    public Drawable getDrawable(String source) {
        int id;
        Context context = MyApplication.getAppContext();
        id = context.getResources().getIdentifier(source, "drawable", context.getPackageName());

        if (id == 0) {
            id = context.getResources().getIdentifier(source, "drawable", "android");
        }

        if (id == 0) {
            return null;
        }
        else {
            Drawable d = context.getResources().getDrawable(id);
            d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
            return d;
        }
    }

}
