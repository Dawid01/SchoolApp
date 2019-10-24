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

    private int lessonNumber;

    private String room;

    private String groupName;

    private String peroid2;

    private String teacher2;

    private String subject2;

    private String className2;

    private String room2;

    private String groupName2;

    private String startTime;

    private String endTime;

    private String status1;

    private String status2;

    public Lesson() {
    }

    public Lesson(String externalID, String peroid, String teacher, String day, String subject, String week, String className, int lessonNumber, String room, String groupName) {
        this.externalID = externalID;
        this.peroid = peroid;
        this.teacher = teacher;
        this.day = day;
        this.subject = subject;
        this.week = week;
        this.className = className;
        this.lessonNumber = lessonNumber;
        this.room = room;
        this.groupName = groupName;
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

    public int getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }
}
