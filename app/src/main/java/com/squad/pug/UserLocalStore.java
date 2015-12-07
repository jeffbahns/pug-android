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
        spEditor.putString("tel_number", user.tel_number);

        spEditor.putString("sex", user.sex);
        spEditor.putInt("age", user.age);
        spEditor.putString("password", user.password);
        spEditor.commit();
    }
    public User getUsername() {
        String username = userLocalDatabase.getString("username", "");
        String name = userLocalDatabase.getString("name", "");
        String sex = userLocalDatabase.getString("sex", "");
        int age = userLocalDatabase.getInt("age", -1);
        String password = userLocalDatabase.getString("password", "");

        User storedUsername = new User(username);

        return storedUsername;
    }

    public User getLoggedInUser(){
    //public User getloggedinuser(){
        String username = userLocalDatabase.getString("username", "demo");
        String name = userLocalDatabase.getString("name", "test");
        String tel_number = userLocalDatabase.getString("tel_number", "");

        String sex = userLocalDatabase.getString("sex", "male");
        int age = userLocalDatabase.getInt("age", 21);
        String password = userLocalDatabase.getString("password", "test");

        User storedUser = new User(username, name, tel_number, sex, age, password);

        return storedUser;

    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }


    public boolean getUserLoggedIn(){
        if (userLocalDatabase.getBoolean("loggedIn", false)) {
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
    public void storeGameData(Game game){
        SharedPreferences.Editor gameEditor = userLocalDatabase.edit();
        gameEditor.putInt("id", game.id);
        gameEditor.putString("user", game.user);
        gameEditor.putString("time", game.time);
        gameEditor.putString("date", game.date);
        gameEditor.putInt("num_players", game.num_players);
        gameEditor.putString("players_attending", game.players_attending);

        gameEditor.putString("location", game.location);
        gameEditor.commit();

    }
    public void storeID(Game game){

        SharedPreferences.Editor gameEditor = userLocalDatabase.edit();
        gameEditor.putInt("id", game.id);
        gameEditor.commit();

    }
    public void clearGameData(){
        SharedPreferences.Editor gameEditor = userLocalDatabase.edit();
        gameEditor.clear();
        gameEditor.commit();

    }
    public Game getGame() {

        int id = userLocalDatabase.getInt("id", 0);
        String user = userLocalDatabase.getString("user", "");
        String time = userLocalDatabase.getString("time", "");
        String date = userLocalDatabase.getString("date", "");
        int num_players = userLocalDatabase.getInt("num_players", -1);
        String players_attending = userLocalDatabase.getString("players_attending", "");

        String location = userLocalDatabase.getString("location", "");

        Game storedGame = new Game(id, user, time, date, num_players, players_attending, location);

        return storedGame;
    }


}

