package com.szczepaniak.dawid.appezn.Models;

import java.util.List;

public class LessonList {

    List<Lesson> lessonList;

    public LessonList(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

    public List<Lesson> getLessonList() {
        return lessonList;
    }

    public void setLessonList(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }
}
