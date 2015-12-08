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

    @SerializedName("formatted_address")
    public String address;

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

    public SearchItemModel(GeometryData geometryData, String ic, String idString,
                           String nameString, String place_id) {
        geometry = geometryData;
        icon = ic;
        id = idString;
        name = nameString;
        placeId = place_id;

    }

    public String getStringLatLng () {
        String stringLat = String.valueOf(geometry.getLocation1().getLat());
        String stringLng = String.valueOf(geometry.getLocation1().getLng());

        String stringLatLng = stringLat + "," + stringLng;
        System.out.println("STRING LAT LNG" + stringLatLng);
        return stringLatLng;
    }

    public ArrayList<String> getItemModelStringArray() {

        // Indexes: 0: geometry 1: icon 2: id 3: name 4: placeId 5: directions 6: stringLatLng
        // trevor** icon,id,name,placeId are already strings, toString=unnecessary
        ArrayList<String> StringArray = new ArrayList<>();
        StringArray.add(geometry.toString());
        StringArray.add(icon);
        StringArray.add(id);
        StringArray.add(name);
        StringArray.add(placeId);
        StringArray.add(address);
        StringArray.add(getStringLatLng());
        return StringArray;
    }

    // just for testing
    public void arrayIterator(ArrayList<String> arr) {
        for (int i = 0; i < arr.size(); i++) {
            try {
                System.out.println(arr.get(i));
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }


    public LatLng getCourtLatLng() {
        LatLng courtLatLng = new LatLng(geometry.getLocation1().getLat(), geometry.getLocation1().getLng());
        return courtLatLng;
    }

    // takes map param and injects a a new court marker
    public void populateMapWithModel(GoogleMap mMap, final Context mContext, HashMap<String, SearchItemModel> markerMap) {
        int randomColor;
        if (gamesExist()) {
            randomColor = 0;
        } else {
            randomColor = 3;
        }

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
        //Game game = new Game(name);
        //searchLocation(game, mContext);

    }

    public void getGamesFromDatabase(Context mContext, GoogleMap mMap, HashMap<String, SearchItemModel> markerMap) {
        getID(new Game(name), mContext, mMap, markerMap);
        //int id = 1;
        //Game game = new Game(id, name);
        /*
        for(int i = 0; i < 4; i++ ) {
            searchLocation( new Game(i, name), mContext, mMap, markerMap);
        }
        printGames();
        */
    }

    public boolean gamesExist() {
        return (gamesList.size() > 0);
    }

    public void printGames() {
        for (int i = 0; i < gamesList.size(); i++) {

            gamesList.get(i).print();
        }
    }

    public void getID(Game game, final Context mContext, final GoogleMap mMap, final HashMap<String, SearchItemModel> markerMap) {
        ServerRequests serverRequests = new ServerRequests(mContext);
        serverRequests.getIDInBackground(game, new GetGameCallback() {
            @Override
            public void done(Game returnedGame) {
                if (returnedGame == null) {
                    //createGame(userLocalStore.getGame());
                } else {
                    //userLocalStore.storeID(returnedGame);
                    //Game ngame = userLocalStore.getGame();
                    int court_id = returnedGame.id + 1;  //number of games at the court
                    for(int i = 0; i < court_id; i++ ) {
                        searchLocation( new Game(i, name), mContext, mMap, markerMap);
                    }
                }
                //return returnedGame;
            }
        });
    }

    // test print routine
    public void print() {
        System.out.println("Name: " + name);
        System.out.println("Location: " + geometry.getLocation1().getLat().toString() + ", " + geometry.getLocation1().getLng().toString());
        System.out.println("Address: " + address);
    }

    public void searchLocation(Game game, final Context mContext, final GoogleMap mMap, final HashMap<String, SearchItemModel> markerMap) {
        ServerRequests serverRequests = new ServerRequests(mContext);
        gamesList = new ArrayList<>();
        serverRequests.fetchGameDataInBackground(game, new GetGameCallback() {
            @Override
            public void done(Game returnedGame) {
                if (returnedGame != null) {
                    try {
                        gamesList.add(returnedGame);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                populateMapWithModel(mMap, mContext, markerMap);
            }
        });
    }



}