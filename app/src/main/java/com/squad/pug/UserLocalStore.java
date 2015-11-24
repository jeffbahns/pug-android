package com.squad.pug;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Bryanpenaloza on 11/11/15.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("username", user.username);
        spEditor.putString("name", user.name);
        spEditor.putString("sex", user.sex);
        spEditor.putInt("age", user.age);
        spEditor.putString("password", user.password);
    }

    public User getLoggedInUser(){
        String username = userLocalDatabase.getString("username", "demo");
        String name = userLocalDatabase.getString("name", "test");
        String sex = userLocalDatabase.getString("sex", "male");
        int age = userLocalDatabase.getInt("age", 21);
        String password = userLocalDatabase.getString("password", "test");

        User storedUser = new User(username, name, sex, age, password);

        return storedUser;

    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }


    public boolean getUserLoggedIn(){
        if (userLocalDatabase.getBoolean("LoggedIn", false)) {
            return true;
        }else{
            return false;
        }
    }
    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();

    }


}

