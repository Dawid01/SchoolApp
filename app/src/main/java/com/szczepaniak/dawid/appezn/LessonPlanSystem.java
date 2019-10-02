package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.szczepaniak.dawid.appezn.Adapters.LessonsAdapter;
import com.szczepaniak.dawid.appezn.Models.Class;
import com.szczepaniak.dawid.appezn.Models.ClassList;
import com.szczepaniak.dawid.appezn.Models.DoubleLesson;
import com.szczepaniak.dawid.appezn.Models.Lesson;
import com.szczepaniak.dawid.appezn.Models.LessonList;
import com.szczepaniak.dawid.appezn.Models.PeriodList;

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
    private Spinner spiner;
    private String day = "10000";

    public LessonPlanSystem(final RecyclerView lessonsView, TabLayout days, Spinner spiner, Context context) {
        this.lessonsView = lessonsView;
        this.days = days;
        this.spiner = spiner;
        this.context = context;
        api = RetroClient.getApiService();

        loadLessons();

        days.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                dayOfWeek = tab.getPosition() + 1;
                switch (tab.getPosition()){

                    case 0:
                        day = "10000";
                        break;
                    case 1:
                        day = "01000";
                        break;
                    case 2:
                        day = "00100";
                        break;
                    case 3:
                        day = "00010";
                        break;
                    case 4:
                        day = "00001";
                        break;
                }
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


    public void loadClasses(){

        lessonsView.removeAllViews();

        retrofit2.Call<ClassList> classListCall = api.getClassList(100);

        classListCall.enqueue(new Callback<ClassList>() {
            @Override
            public void onResponse(Call<ClassList> call, Response<ClassList> response) {

                if(response.isSuccessful()){

                    List<Class> classList = response.body().getClasses();

                    if(classList != null){

                        List<String> list = new ArrayList<>();
                        for(Class c : classList){

                            list.add(c.getName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                                android.R.layout.simple_spinner_dropdown_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spiner.setAdapter(adapter);


                        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                loadLessons();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                            }

                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<ClassList> call, Throwable t) {

            }
        });


    }


    void loadLessons(){

        lessonsView.removeAllViews();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        lessonsView.setLayoutManager(layoutManager);

        String c = "1 AG";

        try{
            c= spiner.getSelectedItem().toString();
        }catch (NullPointerException e){}

        retrofit2.Call<LessonList> lessonListCall = api.getLessons(c,day,100, "lessonNumber,asc");

        lessonListCall.enqueue(new Callback<LessonList>() {
            @Override
            public void onResponse(Call<LessonList> call, Response<LessonList> response) {

                if(response.isSuccessful()){

                    final List<Lesson> lessons = response.body().getLessons();

                    LessonsAdapter lessonsAdapter = new LessonsAdapter(converLessons(lessons), context);
                    lessonsView.setAdapter(lessonsAdapter);

                }
            }

            @Override
            public void onFailure(Call<LessonList> call, Throwable t) {

            }
        });



    }


    List<Lesson> converLessons(List<Lesson> lessons){

        int count = lessons.size();

        for(int i = 0; i < count; i++) {

            Lesson lesson = lessons.get(i);
            int lessonNumber = lesson.getLessonNumber();

            if (i + 1 != lessons.size()) {

                Lesson lesson2 = lessons.get(i + 1);

                if (lesson2.getLessonNumber() == lessonNumber) {

                    lesson.setClassName2(lesson2.getClassName());
                    lesson.setGroupName2(lesson2.getGroupName());
                    lesson.setPeroid2(lesson2.getPeroid());
                    lesson.setTeacher2(lesson2.getTeacher());
                    lesson.setSubject2(lesson2.getSubject());
                    lesson.setRoom2(lesson2.getRoom());
                    lessons.remove(lesson2);
                    count--;
                }

            }
        }

//        try {
//            int index = lessons.get(0).getLessonNumber();
//            if (index != 0){
//
//                for(int i = 0; i < index; i++){
//
//                    Lesson lesson =  new Lesson();
//                    lesson.setSubject("---------");
//                    lesson.setLessonNumber(i);
//                    lessons.add(i, lesson);
//                }
//            }
//        }catch (IndexOutOfBoundsException e){}

        return lessons;

    }


}
