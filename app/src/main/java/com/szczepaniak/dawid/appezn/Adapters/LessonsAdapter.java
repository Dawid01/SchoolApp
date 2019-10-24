package com.szczepaniak.dawid.appezn.Adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.szczepaniak.dawid.appezn.Models.Lesson;
import com.szczepaniak.dawid.appezn.R;

import java.util.List;


public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.ViewHolder>{

    private List<Lesson> lessons;
    private Context context;
    private  boolean addClassNamae;


    public LessonsAdapter(List<Lesson> Lessons, boolean addClassNamae, Context context) {
        this.lessons = Lessons;
        this.addClassNamae = addClassNamae;
        this.context = context;

    }

    @Override
    public LessonsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson, parent, false);
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
        holder.status.setText(lesson.getStatus1());
        holder.status2.setText(lesson.getStatus2());

        if (addClassNamae) {

            holder.name.setText(lesson.getClassName() + " " + holder.name.getText());
        }

        try{

            if(position != lessons.size() - 1) {
                Lesson l = lessons.get(position + 1);
                String nexTime = l.getStartTime();
                String firstTime = lesson.getEndTime();
                holder.pause.setVisibility(View.VISIBLE);
                holder.pause.setText(getPauseTime(firstTime, nexTime));
            }
        }catch (IndexOutOfBoundsException e){}

        holder.setIsRecyclable(false);

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

        public TextView pause;

        public TextView status;
        public TextView status2;


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
            pause = itemView.findViewById(R.id.pause);

            status = itemView.findViewById(R.id.status);
            status2 = itemView.findViewById(R.id.status2);

        }
    }

    private String getPauseTime(String s1, String s2){

        String s1h = s1.substring(0,2);
        s1h = s1h.replace(":", "");
        String s1m = s1.substring(Math.max(s1.length() - 2, 0));
        int t1 = Integer.parseInt(s1h) * 60 + Integer.parseInt(s1m);

        String s2h = s2.substring(0,2);
        s2h = s2h.replace(":", "");
        String s2m = s2.substring(Math.max(s2.length() - 2, 0));
        int t2 = Integer.parseInt(s2h) * 60 + Integer.parseInt(s2m);

        int t = t2 - t1;

        return "Przerwa: " + t + " min";
    }
}