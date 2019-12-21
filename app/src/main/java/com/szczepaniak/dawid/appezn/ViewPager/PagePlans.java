package com.szczepaniak.dawid.appezn.ViewPager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;

import com.szczepaniak.dawid.appezn.LessonPlanSystem;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.Singleton;

public class PagePlans {

    private Context context;
    private View pageView;

    private RecyclerView lessonsView;
    private LessonPlanSystem lessonPlanSystem;
    private Spinner weekSpinner;
    private ImageView next;
    private ImageView back;
    private Singleton singleton;

    public PagePlans(Context context, View pageView) {
        this.context = context;
        this.pageView = pageView;

        singleton = Singleton.getInstance();
        weekSpinner = pageView.findViewById(R.id.spinner_weeks);
        next = pageView.findViewById(R.id.week_next);
        back = pageView.findViewById(R.id.week_back);
        lessonsView = pageView.findViewById(R.id.lessons_view);
        TabLayout days = pageView.findViewById(R.id.week_days);
        lessonPlanSystem = new LessonPlanSystem(lessonsView, days, singleton.getSpinnerClass(), singleton.getSpinnerTypes(), next, back, weekSpinner, context);
    }

}
