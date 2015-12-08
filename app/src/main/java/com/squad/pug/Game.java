package com.squad.pug;

/**
 * Created by Trevor on 12/4/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;


public class Game implements Parcelable {
    String user, time, date, players_attending, location;
    int num_players;
    public int id;

    public Game (int id, String user, String time, String date, int num_players, String players_attending,
                 String location){
        this.id = id;
        this.user = user;
        this.time = time;
        this.date = date;
        this.num_players = num_players;
        this.players_attending = players_attending;
        this.location = location;
    }

    public Game(int id, String location) {
        this.id = id;
        this.location = location;
        this.time = "";
        this.date = "";
        this.num_players = -1;
        this.players_attending = "";

    }
    public Game(int id) {
        this.location = "";
        this.id = id;
        this.time = "";
        this.date = "";
        this.num_players = -1;
        this.players_attending = "";

    }
    public Game(String location) {
        this.location = location;
        this.id = 0;
        this.time = "";
        this.date = "";
        this.num_players = -1;
        this.players_attending = "";

    }
    public Game(int id, String location, String user) {
        this.id = id;
        this.location = location;
        this.user = user;
        this.time = "";
        this.date = "";
        this.num_players = -1;
        this.players_attending = "";
    }

    public void print() {
        System.out.println(id);
        System.out.println(user);
        System.out.println(time);
        System.out.println(date);
        System.out.println(num_players);
        System.out.println(players_attending);

        System.out.println(location);
    }

    // This is where you write the values you want to save to the `Parcel`.
    // The `Parcel` class has methods defined to help you save all of your values.
    // Note that there are only methods defined for simple values, lists, and other Parcelable objects.
    // You may need to make several classes Parcelable to send the data you want.
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(user);
        out.writeString(time);
        out.writeString(date);
        out.writeInt(num_players);
        out.writeString(players_attending);

        out.writeString(location);

    }

    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.
    private Game(Parcel in) {
        user = in.readString();
        time = in.readString();
        date = in.readString();
        num_players = in.readInt();
        players_attending = in.readString();

        location = in.readString();
    }


    // In the vast majority of cases you can simply return 0 for this.
    // There are cases where you need to use the constant `CONTENTS_FILE_DESCRIPTOR`
    // But this is out of scope of this tutorial
    @Override
    public int describeContents() {
        return 0;
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Game> CREATOR
            = new Parcelable.Creator<Game>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

}
