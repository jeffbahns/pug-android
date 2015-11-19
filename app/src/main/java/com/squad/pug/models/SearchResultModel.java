package com.squad.pug.models;

import com.google.android.gms.maps.GoogleMap;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchResultModel {
    @SerializedName("results")
    public ArrayList<SearchItemModel> courts;

    // iterates through courts and calls their inner populate function
    public void populateMapWithModels(GoogleMap mMap) {
        for( int i = 0; i < courts.size(); i++ ) {
            courts.get(i).populateMapWithModel(mMap);
        }

    }

    // for general testing purposes
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
