package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

public class Methods {

    public static HashSet<String> getCookies(Context context) {
        SharedPreferences mcpPreferences = getSKSharedPreferences(context);
        return (HashSet<String>) mcpPreferences.getStringSet("cookies", new HashSet<String>());
    }

    public static boolean setCookies(Context context, HashSet<String> cookies) {
        SharedPreferences mcpPreferences = getSKSharedPreferences(context);
        SharedPreferences.Editor editor = mcpPreferences.edit();
        return editor.putStringSet("cookies", cookies).commit();
    }

    static SharedPreferences getSKSharedPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("cookies",Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
