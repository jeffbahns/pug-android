package com.squad.pug;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squad.pug.models.LocationData;
import com.squad.pug.models.SearchResultModel;
import com.squad.pug.services.RESTAPIClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public SearchResultModel courtsResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // zoom clashes with our UI
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            mMap.setMyLocationEnabled(true);
        }

    }

    /* public void gotoCourtsList(View view){
         Intent intent = new Intent(this, PutCourtListClassHere)
     }*/

    public void populateMap(View view) throws IOException {
        Location myLocation = mMap.getMyLocation();
        final String locationLatLong = String.valueOf(myLocation.getLatitude()) + "," + String.valueOf(myLocation.getLongitude());
        new AsyncTask<String, String, SearchResultModel>(){
            @Override
            protected SearchResultModel doInBackground(String... params) {
                SearchResultModel model = new SearchResultModel();

                try {
                    System.out.println("Getting courts..");

                    // Instantiate Gson
                    Gson gson = new Gson();

                    // Building query string
                    String rad = "&radius=5000";
                    String type = "&keyword=basketball+court";

                    // Make HTTP Connection & Request
                    String urlString = AppDefines.urlStringBase + AppDefines.testLocations[0] + rad + type + "&key=" + AppDefines.GOOGLE_SERVER_API;
                    URL url = new URL(urlString);
                    URLConnection conn = url.openConnection();
                    InputStream is = conn.getInputStream();

                    // Deserialize data into SearchResultModel, which is an ArrayList<SearchItemModel>!
                    Reader reader = new InputStreamReader(is);
                    model = gson.fromJson(reader, SearchResultModel.class);
                    courtsResult = model;
                    courtsResult.getArrayOfNames();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("COURT REQUEST FAILED");
                }
                return model;
            }

            @Override
            protected void onPostExecute(SearchResultModel result){
                // Populate my first map
                // have to send it activity context so it can modify the state
                // of MapsActivity from outside of it
                final Context mContext = MapsActivity.this.getApplicationContext();
                result.populateMapWithModels(mMap, mContext);

                // console print test
                result.print();
            }
        }.execute("");
    }

    public void openProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openGames(View view) {
        Intent intent = new Intent(this, GamesActivity.class);
        startActivity(intent);
    }

    public void openCreateGame(View view) {
        Intent intent = new Intent(this, CreateGameActivity.class);
        intent.putExtra("courtNames", courtsResult.getArrayOfNames());
        startActivity(intent);
    }

    public void pickCourtSearchLocation(View view) {
        // Build a place builder
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {


                Gson gson = new GsonBuilder()
                        .create();

                /*    Retrofit RESTAdapter = new Retrofit.Builder()
                            .baseUrl(AppDefines.COURTS_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();   */

                // Set up picked location, and move camera accordingly
                Place place = PlacePicker.getPlace(data, this);
                LatLng placeLatLng = place.getLatLng();
                LocationData loc = new LocationData(placeLatLng);
                String strlocationlat = loc.getLat().toString();
                String strlocationlong = loc.getLng().toString();
                String strlocation = strlocationlat + "," + strlocationlong;
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 14));

                // Convert location to string and pass to maps http request
                //         IGetCourtsApi mApi = RESTAdapter.create(IGetCourtsApi.class);

                // Temps / Test values (Distance in meters for radius)
                String rad = "2000";
                String currentSearchTerm = "court";


                // Call to Search using current location as a string

                RESTAPIClient.getCourtsProvider()
                        .GetCourts(strlocation, currentSearchTerm, rad, AppDefines.GOOGLE_SERVER_API)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<SearchResultModel>() {
                            @Override
                            public void onCompleted() {



                            }

                            @Override
                            public void onError(Throwable e) {

                                int i = 0;

                            }

                            @Override
                            public void onNext(SearchResultModel courtsSearchResults) {

                                int index = 0;
                                // Testing to see if I have data
                                String testing = courtsSearchResults.courts.get(index).id;

                                Toast.makeText(getApplicationContext(), testing,
                                        Toast.LENGTH_LONG).show();
                                Log.v("***********************", "onNext called.");


                       /*         while(courtsSearchResults.courts.get(index) == null) {
                                    mMap.addMarker(new MarkerOptions()
                                    .position(courtsSearchResults.courts.get(index).getCourtLatLng())
                                    .draggable(false));

                                }
                                */
                            }
                        });
//                Log.d("Test**", strlocation);
//                Toast.makeText(getApplicationContext(), strlocation,
//                       Toast.LENGTH_LONG).show();


            }
        }
    }
}



