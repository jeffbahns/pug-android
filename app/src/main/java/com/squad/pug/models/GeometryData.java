package com.squad.pug.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeometryData {

    @SerializedName("location")
    @Expose
    private LocationData location;

    /**
     *
     * @return
     * The location
     */

    public GeometryData(LocationData location){
        this.location = location;
    }

    public LocationData getLocation1() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(LocationData location) {
        this.location = location;
    }

}