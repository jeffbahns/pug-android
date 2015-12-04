package com.squad.pug.services.providers;

import com.squad.pug.models.SearchResultModel;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface ICourtsProvider {

    // DEFINE API ENDPOINTS

    @GET("/maps/api/place/nearbysearch/json")
    Observable<SearchResultModel> GetCourts(
            @Query("location") String strlocation,
            @Query("name") String name,
            @Query("rad") String radius,
            @Query("key") String appKey
    );
  /*  @GET("/maps/api/place/nearbysearch")
    CourtsData getCourtsFromGoogleSync(
            @Query("json") String location,
            @Query("APPKEY") String appKey
    );  */
}
