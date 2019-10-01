package com.szczepaniak.dawid.appezn.Adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.Models.DoubleLesson;
import com.szczepaniak.dawid.appezn.Models.Lesson;
import com.szczepaniak.dawid.appezn.Models.Period;
import com.szczepaniak.dawid.appezn.Models.PeriodList;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.ViewHolder>{

    private List<Lesson> lessons;
    private Context context;


    public LessonsAdapter(List<Lesson> Lessons, Context context) {
        this.lessons = Lessons;
        this.context = context;

    }

    @Override
    public LessonsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_plan, parent, false);
        return new LessonsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final LessonsAdapter.ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);

        if (lesson.getClassName2() != null  && !lesson.getClassName2().equals("")) {

            holder.line.setVisibility(View.VISIBLE);
            holder.lesson2.setVisibility(View.VISIBLE);
            holder.name2.setText(lesson.getSubject2());
            holder.room2.setText(lesson.getRoom2());
            holder.info2.setText(lesson.getTeacher2());
            holder.group2.setText(lesson.getGroupName2());
            holder.group.setText(lesson.getGroupName());

        }

        try {
            if (!lesson.getGroupName().equals("Cala klasa")) {
                holder.group.setText(lesson.getGroupName());
            }
        }catch (NullPointerException e){}

        holder.index.setText("" + lesson.getLessonNumber());
        holder.name.setText(lesson.getSubject());
        holder.time.setText(lesson.getPeroid());
        holder.room.setText(lesson.getRoom());
        holder.info.setText(lesson.getTeacher());

    }

    @Override public int getItemCount() {
        return lessons.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView index;
        public TextView time;
        public TextView name;
        public TextView room;
        public TextView info;
        public TextView group;

        public TextView name2;
        public TextView room2;
        public TextView info2;
        public TextView group2;

        public ConstraintLayout lesson2;
        public ImageView line;


        public ViewHolder(View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.index);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            room = itemView.findViewById(R.id.room);
            info = itemView.findViewById(R.id.info);
            group = itemView.findViewById(R.id.group);

            name2 = itemView.findViewById(R.id.name2);
            room2 = itemView.findViewById(R.id.room2);
            info2 = itemView.findViewById(R.id.info2);
            group2 = itemView.findViewById(R.id.group2);

            lesson2 = itemView.findViewById(R.id.lesson2);
            line = itemView.findViewById(R.id.line);

        }
    }
}