package com.squad.pug.models;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.SerializedName;

public class SearchItemModel {

    @SerializedName("geometry")
    public GeometryData geometry;

    @SerializedName("icon")
    public String icon;

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

//    @SerializedName("opening_hours")
//    public OpeningHours openingHours;
 //   @SerializedName("photos")
 //   public List<Photo> photos = new ArrayList<Photo>();

    @SerializedName("place_id")
    public String placeId;

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

    // takes map param and injects a new marker ( court )
    public void populateMapWithModel(GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions()
                .position(getCourtLatLng())
                .draggable(false)
                .title("Hello World!"));
    }

    // for general testing purposes
    public void print() {
        System.out.println("Name: " + name);
        System.out.println("Location: " + geometry.getLocation1().getLat().toString() + ", " + geometry.getLocation1().getLng().toString());

    }


}