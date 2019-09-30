package com.szczepaniak.dawid.appezn.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szczepaniak.dawid.appezn.Models.Lesson;
import com.szczepaniak.dawid.appezn.R;

import java.util.ArrayList;
import java.util.List;


public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.ViewHolder>{

    private List<Lesson> lessons;
    private Context context;

    public LessonsAdapter(List<Lesson> Lessons, Context context) {
        this.lessons = Lessons;
        this.context = context;
    }

    @Override
    public LessonsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan, parent, false);
        return new LessonsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final LessonsAdapter.ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);

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

        public ViewHolder(View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.index);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            room = itemView.findViewById(R.id.room);
            info = itemView.findViewById(R.id.info);
        }
    }
}