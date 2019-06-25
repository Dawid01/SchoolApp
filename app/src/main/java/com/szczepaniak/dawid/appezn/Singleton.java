package com.szczepaniak.dawid.appezn;

public class Singleton {
    private static final Singleton ourInstance = new Singleton();

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
}
