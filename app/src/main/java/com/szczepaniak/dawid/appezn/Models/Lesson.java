package com.szczepaniak.dawid.appezn.Models;

public class Lesson {

    private Long id;

    private String externalID;

    private String peroid;

    private String teacher;

    private String day;

    private String subject;

    private String week;

    private String className;

    private String lessonNumber;

    private String room;

    public Lesson() {
    }

    public Lesson(String externalID, String peroid, String teacher, String day, String subject, String week, String className, String lessonNumber, String room) {
        this.externalID = externalID;
        this.peroid = peroid;
        this.teacher = teacher;
        this.day = day;
        this.subject = subject;
        this.week = week;
        this.className = className;
        this.lessonNumber = lessonNumber;
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    public String getPeroid() {
        return peroid;
    }

    public void setPeroid(String peroid) {
        this.peroid = peroid;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
