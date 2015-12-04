package com.squad.pug.services;

import com.squad.pug.AppDefines;
import com.squad.pug.services.providers.ICourtsProvider;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by jfisher on 11/7/15.
 */
public class RESTAPIClient {
    private static ICourtsProvider courtsProvider;

    public static ICourtsProvider getCourtsProvider(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDefines.COURTS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        courtsProvider = retrofit.create(ICourtsProvider.class);

        return courtsProvider;
    }
}
