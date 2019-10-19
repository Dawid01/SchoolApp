package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class WeekSelector {

    private Spinner week;
    private ArrayList<String> weeks;
    Context context;

    public WeekSelector(View back, View next, final Spinner week, Context context) {
        this.week = week;
        this.context = context;
        weeks = new ArrayList<>();
        loadWeeks();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeWeek(1);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeWeek(-1);
            }
        });


    }


    void loadWeeks(){

        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < 5; i++){

            String time = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 6);
            time = time + " - " + format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            weeks.add(time);

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, weeks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        week.setAdapter(adapter);

    }

    void changeWeek(int i){

        int index = week.getSelectedItemPosition() + i;

        if(index >= 0 && index <= weeks.size() - 1){

            week.setSelection(index);
        }
    }


}
