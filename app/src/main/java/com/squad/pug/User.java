package com.squad.pug;

/**
 * Created by Bryanpenaloza on 11/11/15.
 */
public class User {

    public String username, name, sex, password;
    int age;

    public User (String username, String name, String sex, int age,
                 String password){

        this.username = username;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.name = "";
        this.sex = "N/A";
        this.age = -1;
    }
    public User(String username) {
        this.username = username;
    }

}
