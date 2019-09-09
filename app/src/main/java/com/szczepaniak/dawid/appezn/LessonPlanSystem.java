package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.szczepaniak.dawid.appezn.Adapters.LessonsAdapter;
import com.szczepaniak.dawid.appezn.Models.Lesson;
import com.szczepaniak.dawid.appezn.Models.LessonList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonPlanSystem {

    private RecyclerView lessonsView;
    private Context context;
    private ApiService api;
    private TabLayout days;
    private int dayOfWeek = 1;

    public LessonPlanSystem(RecyclerView lessonsView, TabLayout days, Context context) {
        this.lessonsView = lessonsView;
        this.days = days;
        this.context = context;
        api = RetroClient.getApiService();
        loadLessons();

        days.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                dayOfWeek = tab.getPosition() + 1;
                loadLessons();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    void loadLessons(){

        lessonsView.removeAllViews();

        retrofit2.Call<LessonList> lessonsCall = api.getLessons();

        lessonsCall.enqueue(new Callback<LessonList>() {
            @Override
            public void onResponse(Call<LessonList> call, Response<LessonList> response) {

                if(response.isSuccessful()){

                    LessonList lessonList = selectLessons(response.body());
                    if(lessonList.getLessonList().size() != 0) {

                        int firstLessonNumber = lessonList.getLessonList().get(0).getLessonNumber();
                        if (firstLessonNumber != 0) {

                            for (int i = 0; i < firstLessonNumber; i++) {
                                Lesson emptyLeson = new Lesson();
                                emptyLeson.setLessonNumber((firstLessonNumber - 1) - i);
                                emptyLeson = setLesonTime(emptyLeson);
                                lessonList.getLessonList().add(0, emptyLeson);
                            }
                        }
                    }

                    LessonsAdapter lessonsAdapter = new LessonsAdapter(lessonList.getLessonList(), context);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    lessonsView.setLayoutManager(layoutManager);
                    lessonsView.setAdapter(lessonsAdapter);
                }
            }

            @Override
            public void onFailure(Call<LessonList> call, Throwable t) {

            }
        });

    }

    private LessonList selectLessons(LessonList lessonList){

        List<Lesson> lessons = new ArrayList<>();
         for (Lesson lesson : lessonList.getLessonList()) {

                if (lesson.getWeekDay() == dayOfWeek) {
                    lessons.add(lesson);
                }
            }

        lessonList.setLessonList(lessons);
        return lessonList;

    }


    private Lesson setLesonTime(Lesson lesson){

        switch (lesson.getLessonNumber()){
            case 0:
                lesson.setTime("7:10 - 7:55");
                break;
            case 1:
                lesson.setTime("8:00 - 8:45");
                break;
            case 2:
                lesson.setTime("8:55 - 9:40");
                break;
            case 3:
                lesson.setTime("9:50 - 10:35");
                break;
            case 4:
                lesson.setTime("10:45 - 11:30");
                break;
            case 5:
                lesson.setTime("11:40 - 12:25");
                break;
            case 6:
                lesson.setTime("12:40 - 13:25");
                break;
            case 7:
                lesson.setTime("13:35 - 14:20");
                break;
            case 8:
                lesson.setTime("14:30 - 15:15");
                break;
            case 9:
                lesson.setTime("15:25 - 16:10");
                break;
            case 10:
                lesson.setTime("16:20 - 17:05");
                break;
            case 11:
                lesson.setTime("17:10 - 17:55");
                break;
        }

        return lesson;
    }
}
