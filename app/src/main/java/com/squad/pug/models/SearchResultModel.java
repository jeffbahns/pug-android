package com.squad.pug.models;

import com.google.android.gms.maps.GoogleMap;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchResultModel {
    @SerializedName("results")
    public ArrayList<SearchItemModel> courts;

    // iterates through courts and calls their inner populate function
    public void populateMapWithModels(GoogleMap mMap) {
        for( int i = 0; i < courts.size(); i++ )
            courts.get(i).populateMapWithModel(mMap);
    }

    // used to return a list of court names for autocomplete text purposes
    public ArrayList<String> getArrayOfNames() {
        ArrayList<String> courtNames = new ArrayList<>();
        for( int i = 0; i < courts.size(); i++ )
            courtNames.add(courts.get(i).name);

        return courtNames;
    }

    // test print routine
    public void print() {
        System.out.println("_________________________________________________________________________");
        System.out.println("                           COURT RESULTS");
        System.out.println("_________________________________________________________________________");
        for( int i = 0; i < courts.size(); i++ ) {
            System.out.println("X - - - - - Court " + i + " - - - - - X");
            courts.get(i).print();
            System.out.println(" ");
        }
        System.out.println("_________________________________________________________________________");
    }

}
