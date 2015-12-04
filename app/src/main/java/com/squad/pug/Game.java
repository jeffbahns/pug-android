package com.squad.pug;

/**
 * Created by jeffbahns on 12/2/2015.
 */
public class Game {
    String user, time, date, location;
    int num_players;

    public Game (String user, String time, String date, int num_players,
                 String location){

        this.user = user;
        this.time = time;
        this.date = date;
        this.num_players = num_players;
        this.location = location;
    }
    public Game(String location) {
        this.location = location;
        this.time = "";
        this.date = "";
        this.num_players = -1;
    }

    public void print() {
        System.out.println(user);
        System.out.println(time);
        System.out.println(date);
        System.out.println(num_players);
        System.out.println(location);
    }
}
