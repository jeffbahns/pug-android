package com.squad.pug.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Trevor on 11/5/2015.
 */
public class SearchResultModel {
    @SerializedName("results")
    public ArrayList<SearchItemModel> courts;
}
