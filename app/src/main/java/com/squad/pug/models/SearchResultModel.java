package com.squad.pug.models;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.gson.annotations.SerializedName;
import com.squad.pug.Game;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultModel {
    @SerializedName("results")
    public ArrayList<SearchItemModel> courts;

    // iterates through courts and calls their inner populate function
    public void populateMapWithModels(GoogleMap mMap, Context mContext, HashMap<String, SearchItemModel> markerMap) {
        for( int i = 0; i < courts.size(); i++ )
            courts.get(i).populateMapWithModel(mMap, mContext, markerMap);
    }

    // used to return a list of court names for autocomplete text purposes
    public ArrayList<String> getArrayOfNames() {
        ArrayList<String> courtNames = new ArrayList<>();
        for( int i = 0; i < courts.size(); i++ )
            courtNames.add(courts.get(i).name);

        return courtNames;
    }

    public ArrayList<Game> grabGames() {
        ArrayList<Game> localGames = new ArrayList<>();
        for (int i = 0; i < courts.size(); i++) {
            for (int j = 0; j < courts.get(i).gamesList.size(); j++) {
                localGames.add(courts.get(i).gamesList.get(j));
            }
        }
        return localGames;
    }

    public void grabGamesFromDatabaseForEachCourt(Context mContext, GoogleMap mMap, HashMap<String, SearchItemModel> markerMap) {
        for( int i = 0; i < courts.size(); i++ ) {
            courts.get(i).getGamesFromDatabase(mContext, mMap, markerMap);
        }
    }

    // test print routine
    public void print() {
        System.out.println("_________________________________________________________________________");
        System.out.println("                           COURT RESULTS");
        System.out.println("_________________________________________________________________________");
        for( int i = 0; i < courts.size(); i++ ) {
            System.out.println("X - - - - - Court " + i + " - - - - - X");
            courts.get(i).print();
            courts.get(i).printGames();
            System.out.println(" ");
        }
        System.out.println("_________________________________________________________________________");
    }

}
