package com.squad.pug;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jfisher on 11/7/15.
 */
public class AppDefines {

    public static final String COURTS_BASE_URL = "https://maps.googleapis.com";
    public static final String GOOGLE_API = "AIzaSyCtPXuVo1-lb_bH_3y7_s2LExqVW5rfIhk";
    public static final String GOOGLE_SERVER_API = "AIzaSyAtIFZ1uG6Iad_xYQMNNnrfAdJsz6TSesM";
    public static final String urlStringBase = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    public static final String[] testLocations = {"37.7743902,-122.4379349"}; // SF, CA

    // Building quick test location in LatLng type from the above array
    public static final String[] latlong = testLocations[0].split(",");
    public static final double latitude = Double.parseDouble(latlong[0]);
    public static final double longitude = Double.parseDouble(latlong[1]);
    public static final LatLng testLocation = new LatLng(latitude, longitude);

    // This array is temporary, and it just randomizes the icons for now until we have things for the colors to indicate
    public static final int[] court_icons = {R.drawable.court_pin_blue, R.drawable.court_pin_red,
            R.drawable.court_pin_black, R.drawable.court_pin_green,};

    // it works , don't touch it bruh
    public static final String JEFFS_GOOGLE_API_KEY = "AIzaSyA_8dJbAB6sS_taHGQIVSEohJMwehksVEU";

}
