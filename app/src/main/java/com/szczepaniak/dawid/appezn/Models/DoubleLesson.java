package com.szczepaniak.dawid.appezn.Models;

public class DoubleLesson extends Lesson{

    private int index;

    private String peroid2;

    private String teacher2;

    private String subject2;

    private String className2;

    private String room2;

    private String groupName2;


    public DoubleLesson() {
    }

    public DoubleLesson(String externalID, String peroid, String teacher, String day, String subject, String week, String className, int lessonNumber, String room, String groupName, String peroid2, String teacher2, String subject2, String className2, String room2, String groupName2) {
        super(externalID, peroid, teacher, day, subject, week, className, lessonNumber, room, groupName);
        this.peroid2 = peroid2;
        this.teacher2 = teacher2;
        this.subject2 = subject2;
        this.className2 = className2;
        this.room2 = room2;
        this.groupName2 = groupName2;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPeroid2() {
        return peroid2;
    }

    public void setPeroid2(String peroid2) {
        this.peroid2 = peroid2;
    }

    public String getTeacher2() {
        return teacher2;
    }

    public void setTeacher2(String teacher2) {
        this.teacher2 = teacher2;
    }

    public String getSubject2() {
        return subject2;
    }

    public void setSubject2(String subject2) {
        this.subject2 = subject2;
    }

    public String getClassName2() {
        return className2;
    }

    public void setClassName2(String className2) {
        this.className2 = className2;
    }

    public String getRoom2() {
        return room2;
    }

    public void setRoom2(String room2) {
        this.room2 = room2;
    }

    public String getGroupName2() {
        return groupName2;
    }

    public void setGroupName2(String groupName2) {
        this.groupName2 = groupName2;
    }
}
