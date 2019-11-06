package com.szczepaniak.dawid.appezn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.szczepaniak.dawid.appezn.Activities.MainActivity;
import com.szczepaniak.dawid.appezn.Adapters.LessonsAdapter;
import com.szczepaniak.dawid.appezn.Models.Class;
import com.szczepaniak.dawid.appezn.Models.ClassList;
import com.szczepaniak.dawid.appezn.Models.Lesson;
import com.szczepaniak.dawid.appezn.Models.LessonList;
import com.szczepaniak.dawid.appezn.Models.Replacement;
import com.szczepaniak.dawid.appezn.Models.ReplacementList;
import com.szczepaniak.dawid.appezn.Models.Room;
import com.szczepaniak.dawid.appezn.Models.RoomList;
import com.szczepaniak.dawid.appezn.Models.Teacher;
import com.szczepaniak.dawid.appezn.Models.TeacherList;

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
    private Spinner spinner;
    private Spinner typeSpinner;
    private View next;
    private View back;
    private Spinner spinnerWeeks;
    private String day = "10000";
    private int animation = 0;

    private SharedPreferences planPreferences;


    private  ArrayList<String> spinnerTypeItems = new ArrayList<>();

    public LessonPlanSystem(final RecyclerView lessonsView, TabLayout days, Spinner spiner, final Spinner typeSpinner,  View next,  View back, Spinner spinnerWeeks, Context context) {
        this.lessonsView = lessonsView;
        this.days = days;
        this.spinner = spiner;
        this.typeSpinner = typeSpinner;
        this.context = context;
        this.spinnerWeeks = spinnerWeeks;

        new WeekSelector(next,back, spinnerWeeks, context);

        api = RetroClient.getApiService();

        spinnerTypeItems.add("Klasy");
        spinnerTypeItems.add("Nauczyciele");
        spinnerTypeItems.add("Sale");
        planPreferences = context.getSharedPreferences("LessonPLan", Context.MODE_PRIVATE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, spinnerTypeItems);
        typeSpinner.setAdapter(adapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String s = typeSpinner.getSelectedItem().toString();

                if(s.equals("Klasy")){

                    loadClasses();
                    loadLessons();

                }else if(s.equals("Nauczyciele")){
                    loadTeachers();
                    loadLessons();
                }else {
                    loadRooms();
                    loadLessons();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        spinnerWeeks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                loadLessons();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        String s = typeSpinner.getSelectedItem().toString();

        if(s.equals("Klasy")){

            loadClasses();
            loadLessons();

        }else if(s.equals("Nauczyciele")){
            loadTeachers();
            loadLessons();
        }else {
            loadRooms();
            loadLessons();
        }

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
                        spinner.setAdapter(adapter);


                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


    public void loadTeachers(){

        lessonsView.removeAllViews();

        retrofit2.Call<TeacherList> teachersCall = api.getTeachers(100);

        teachersCall.enqueue(new Callback<TeacherList>() {
            @Override
            public void onResponse(Call<TeacherList> call, Response<TeacherList> response) {

                if(response.isSuccessful()) {

                    List<Teacher> teracherList = response.body().getTeachers();

                    if (teracherList != null) {

                        List<String> list = new ArrayList<>();
                        for (Teacher t : teracherList) {

                            list.add(t.getName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                                android.R.layout.simple_spinner_dropdown_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);


                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            public void onFailure(Call<TeacherList> call, Throwable t) {

            }
        });

    }


    public void loadRooms(){

        lessonsView.removeAllViews();

        retrofit2.Call<RoomList> roomsCall = api.getRooms(100);

        roomsCall.enqueue(new Callback<RoomList>() {
            @Override
            public void onResponse(Call<RoomList> call, Response<RoomList> response) {

                if(response.isSuccessful()){

                    List<Room> RoomsList = response.body().getRooms();

                    if (RoomsList != null) {

                        List<String> list = new ArrayList<>();
                        for (Room r : RoomsList) {

                            list.add(r.getName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                                android.R.layout.simple_spinner_dropdown_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);


                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            public void onFailure(Call<RoomList> call, Throwable t) {

            }
        });


    }


    void loadLessons(){

        lessonsView.removeAllViews();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        lessonsView.setLayoutManager(layoutManager);
        String c = "1 AG";

        try{
            c= spinner.getSelectedItem().toString();
        }catch (NullPointerException e){}


        retrofit2.Call<LessonList> lessonListCall;
        String s = typeSpinner.getSelectedItem().toString();
        if(s.equals("Klasy")){
            lessonListCall = api.getLessons(c,day,100, "lessonNumber,asc");
        }else if(s.equals("Nauczyciele")){
            lessonListCall = api.getLessonsByTeacher(c,day,100, "lessonNumber,asc");
        }else {
            lessonListCall = api.getLessonsByRoom(c,day,100, "lessonNumber,asc");

        }

        //retrofit2.Call<LessonList> lessonListCall = api.getLessons(c,day,100, "lessonNumber,asc");

        lessonListCall.enqueue(new Callback<LessonList>() {
            @Override
            public void onResponse(Call<LessonList> call, Response<LessonList> response) {

                if(response.isSuccessful()){

                    if(animation != 0) {
                        loadPeplacements(response.body().getLessons());
                    }else {
                        animation = 1;
                    }
                    runLayoutAnimation(lessonsView);

                }
            }

            @Override
            public void onFailure(Call<LessonList> call, Throwable t) {

            }
        });

    }


    void loadPeplacements(final List<Lesson> lessons){


        try{
            retrofit2.Call<ReplacementList> replacementListCall = api.getReplecements(spinnerWeeks.getSelectedItem().toString(), day, spinner.getSelectedItem().toString());

//            final ProgressDialog lessonDialog = ProgressDialog.show(context, "Loading...",
//                    "Load lessons", true);
            replacementListCall.enqueue(new Callback<ReplacementList>() {
                @Override
                public void onResponse(Call<ReplacementList> call, Response<ReplacementList> response) {

                    if(response.isSuccessful()){

                        //lessonDialog.dismiss();
                        List<Replacement> replacements = response.body().getReplacementList();
                        List<Lesson> newLessons = lessons;

                        for(Replacement r : replacements){

                            for(Lesson l : newLessons){

                                if(l.getLessonNumber() == r.getLessonNumber()){

                                    if(l.getGroupName().equals(r.getGroupName())) {
                                        if(!r.getStatus().equals("odwołane")) {
                                            l.setRoom(r.getRoom());
                                            l.setSubject(r.getSubject());
                                            l.setTeacher(r.getTeacher());
                                        }
                                        l.setStatus1(r.getStatus());
                                        l.setStatus2(r.getStatus());
                                        newLessons.set(newLessons.indexOf(l), l);
                                    }

                                }
                            }
                        }


                        boolean addClassNamae = typeSpinner.getSelectedItem().toString().equals("Klasy");
                        LessonsAdapter lessonsAdapter = new LessonsAdapter(converLessons(newLessons), !addClassNamae, context);
                        lessonsView.setAdapter(lessonsAdapter);

                    }
                }

                @Override
                public void onFailure(Call<ReplacementList> call, Throwable t) {

                    boolean addClassNamae = typeSpinner.getSelectedItem().toString().equals("Klasy");
                    LessonsAdapter lessonsAdapter = new LessonsAdapter(converLessons(lessons), !addClassNamae, context);
                    lessonsView.setAdapter(lessonsAdapter);
                    //lessonDialog.dismiss();
                }
            });

        }catch (NullPointerException e){

            boolean addClassNamae = typeSpinner.getSelectedItem().toString().equals("Klasy");
            LessonsAdapter lessonsAdapter = new LessonsAdapter(converLessons(lessons), !addClassNamae, context);
            lessonsView.setAdapter(lessonsAdapter);
        }

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
                    lesson.setStatus2(lesson2.getStatus2());
                    lessons.remove(lesson2);
                    count--;
                }

            }
        }

        return lessons;

    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);
        recyclerView.setLayoutAnimation(controller);
    }



}
