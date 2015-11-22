package com.squad.pug.models;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.SerializedName;
import com.squad.pug.AppDefines;

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


//    @SerializedName("opening_hours")
//    public OpeningHours openingHours;
//    @SerializedName("photos")
//    public List<Photo> photos = new ArrayList<Photo>();
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
    }

    // test print routine
    public void print() {
        System.out.println("Name: " + name);
        System.out.println("Location: " + geometry.getLocation1().getLat().toString() + ", " + geometry.getLocation1().getLng().toString());
    }

}