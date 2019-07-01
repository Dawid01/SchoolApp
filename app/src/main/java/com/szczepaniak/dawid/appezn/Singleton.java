package com.szczepaniak.dawid.appezn;

public class Singleton {
    private static final Singleton ourInstance = new Singleton();

    private long currentUserID;

    public static Singleton getInstance() {
        return ourInstance;
    }

    private User currentUser;

    private Singleton() {
    }

    public static Singleton getOurInstance() {
        return ourInstance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public long getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(long currentUserID) {
        this.currentUserID = currentUserID;
    }
}
