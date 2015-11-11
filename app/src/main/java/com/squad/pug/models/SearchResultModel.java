package com.squad.pug.models;

import com.google.gson.annotations.SerializedName;
import com.squad.pug.Geometry.Geometry;

import java.util.ArrayList;

/**
 * Created by Trevor on 11/5/2015.
 */
public class SearchResultModel {
    @SerializedName("results")
    ArrayList<Geometry> searchResults;

    public ArrayList<Geometry> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(ArrayList<Geometry> searchResults) {
        this.searchResults = searchResults;
    }
}
