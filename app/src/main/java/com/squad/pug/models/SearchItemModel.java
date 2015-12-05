package com.squad.pug.models;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.SerializedName;
import com.squad.pug.AppDefines;
import com.squad.pug.Game;
import com.squad.pug.GetGameCallback;
import com.squad.pug.ServerRequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SearchItemModel {
    @SerializedName("geometry")
    public GeometryData geometry;

    @SerializedName("icon")
    public String icon;

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("place_id")
    public String placeId;

    public Marker markyMarker;

    public int games = 0;

    public ArrayList<Game> gamesList;

//    @SerializedName("opening_hours")
//    public OpeningHours openingHours;
//    @SerializedName("scope")
//    public String scope;
//    @SerializedName("alt_ids")
//    public List<AltId> altIds = new ArrayList<AltId>();
//    @SerializedName("reference")
//    public String reference;
//    @SerializedName("types")
//    public List<String> types = new ArrayList<String>();
//    @SerializedName("vicinity")
//    public String vicinity;

    // Trevor's temp. constructor to build mock item model

    public SearchItemModel (GeometryData geometryData, String ic, String idString,
                            String nameString, String place_id){
        geometry = geometryData;
        icon = ic;
        id = idString;
        name = nameString;
        placeId = place_id;
        gamesList = new ArrayList<>();
        gamesList.add(new Game("jefff", "12a/23/23", "232f3", 3, "Dorotea"));

    }

    public ArrayList<String> getItemModelStringArray () {

        // Indexes: 0: geometry 1: icon 2: id 3: name 4: placeId
        // trevor** icon,id,name,placeId are already strings, toString=unnecessary
        ArrayList<String> mockItemStringArray = new ArrayList<>();
        mockItemStringArray.add(geometry.toString());
        mockItemStringArray.add(icon);
        mockItemStringArray.add(id);
        mockItemStringArray.add(name);
        mockItemStringArray.add(placeId);

        return mockItemStringArray;
    }
    // just for testing
    public void arrayIterator(ArrayList<String> arr ) {
        for( int i = 0; i < arr.size(); i++ ) {
            try {
                System.out.println(arr.get(i));
            } catch(RuntimeException e ) {
                e.printStackTrace();
            }
        }
    }


    public LatLng getCourtLatLng(){
        LatLng courtLatLng = new LatLng(geometry.getLocation1().getLat(), geometry.getLocation1().getLng());
        return courtLatLng;
    }

    // takes map param and injects a a new court marker
    public void populateMapWithModel(GoogleMap mMap, final Context mContext, HashMap<String, SearchItemModel> markerMap) {
        Random rand = new Random();
        int randomColor = rand.nextInt(4);

        markyMarker = mMap.addMarker(new MarkerOptions()
                .position(getCourtLatLng())
                .draggable(false)
                .title(name)
                .snippet(games + " games active within 24hrs")
                .icon(BitmapDescriptorFactory.fromResource(AppDefines.court_icons[randomColor])));
        //  .icon(BitmapDescriptorFactory.defaultMarker(randomColor)));

        // connects HashMap to SearchItemModel/Map marker
        markerMap.put(markyMarker.getId(), this);

        // server request
        Game game = new Game(name);//
        searchLocation(game, mContext);
        printGames();
        //
    }
    public void printGames() {
        try {
            if (gamesList.size() != 0) {
                for (int i = 0; i < gamesList.size(); i++) {
                    gamesList.get(i).print();
                }
            }
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
    }

    // test print routine
    public void print() {
        System.out.println("Name: " + name);
        System.out.println("Location: " + geometry.getLocation1().getLat().toString() + ", " + geometry.getLocation1().getLng().toString());
    }
    public void searchLocation(Game game, Context mContext){
        ServerRequests serverRequests = new ServerRequests(mContext);
        serverRequests.fetchGameDataInBackground(game, new GetGameCallback() {
            @Override
            public void done(Game returnedGame) {
                if (returnedGame != null) {
                    try {
                        //gamesList.add(returnedGame);
                        returnedGame.print();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



}