package com.squad.pug.models;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.SerializedName;

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
    public void populateMapWithModel(GoogleMap mMap, final Context mContext) {
        Random rand = new Random();
        int randomColor = rand.nextInt(360);

        mMap.addMarker(new MarkerOptions()
                .position(getCourtLatLng())
                .draggable(false)
                .title(name)
                .snippet(games + " games active within 24hrs")
                .icon(BitmapDescriptorFactory.defaultMarker(randomColor)));

        /* responds to marker click, soon will open up fragment with data
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Toast works 100%, probably a place holder until pop up fragment is functioning
                //Toast courtSnippet = Toast.makeText(mContext, name, Toast.LENGTH_SHORT);
                //courtSnippet.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
                //courtSnippet.show();


                         totally busted
                        Fragment frag = new Fragment();
                        frag.startActivity(new Intent(mContext, courtFragment.class));

                return true;

        });*/
    }

    // test print routine
    public void print() {
        System.out.println("Name: " + name);
        System.out.println("Location: " + geometry.getLocation1().getLat().toString() + ", " + geometry.getLocation1().getLng().toString());
    }

}