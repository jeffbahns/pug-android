package com.squad.pug.models;

import com.google.android.gms.maps.model.LatLng;

import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trevor on 11/11/2015.
 */


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


}