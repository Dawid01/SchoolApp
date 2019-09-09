package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

public class LessonPlanSystem {

    private RecyclerView lessonsView;
    private Context context;

    public LessonPlanSystem(RecyclerView lessonsView, Context context) {
        this.lessonsView = lessonsView;
        this.context = context;
    }


}
