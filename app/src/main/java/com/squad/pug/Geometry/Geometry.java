package com.squad.pug.Geometry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.squad.pug.Location.Location1;

/**
 * Created by Trevor on 11/5/2015.
 */
public class Geometry  {

    @SerializedName("location")
    @Expose
    private Location1 location;

    /**
     *
     * @return
     * The location
     */

    public Geometry(Location1 location){
        this.location = location;
    }

    public Location1 getLocation1() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(Location1 location) {
        this.location = location;
    }

}