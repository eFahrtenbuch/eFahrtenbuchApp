package com.example.efahrtenbuchapp.eFahrtenbuch;


/**
 * Singleton Design Pattern
 */
public class UserManager {

    private static final UserManager um = new UserManager();

    public static final UserManager getInstance(){
        return um;
    }

    private User user;


    private UserManager(){}


    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return this.user;
    }
}
