package com.squad.pug.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squad.pug.models.GeometryData;
import com.squad.pug.models.LocationData;
import com.squad.pug.models.SearchItemModel;
import com.squad.pug.models.SearchResultModel;
import com.squad.pug.AppDefines;
import com.squad.pug.Geometry.Geometry;
import com.squad.pug.Location.Location1;
import com.squad.pug.R;
import com.squad.pug.services.RESTAPIClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
  //  public SearchResultModel CourtSearchResults;
    //    private JSONObject CourtsData;
    // private GetCourtsRESTAdapter RESTAdapter = new GetCourtsRESTAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            mMap.setMyLocationEnabled(true);
        }

        // Add a marker in Sydney and move the camera
        LatLng SonomaState = new LatLng(38.3393866, -122.6763699);
        mMap.addMarker(new MarkerOptions().position(SonomaState).title("Marker in Sonoma State"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SonomaState));


    }

    public void populateMap(View view) throws IOException {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    System.out.println("HELLLLLO");
                    String urlString;
                    urlString = "http://api.wunderground.com/api/1868967083e9ca9a/conditions/q/California/Oakland.json";
                    URL url = new URL(urlString);
                    URLConnection conn = url.openConnection();
                    InputStream is = conn.getInputStream();

                    String result = getStringFromInputStream(is);
                    System.out.println(result);
                    /*String city = jsonObject.getString("city");
                    System.out.println(city);*/

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v("test", "Background thread is f*cked");
                }
            }
        });
        thread.start();
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    /* public void gotoCourtsList(View view){
         Intent intent = new Intent(this, PutCourtListClassHere)
     }*/
    public void openProfile(View view) {
        // build the intent
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void openSettings(View view) {
        // build the intent
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openGames(View view) {
        // build the intent
        Intent intent = new Intent(this, GamesActivity.class);
        startActivity(intent);
    }
    public void openCreateGame(View view) {
        Intent intent = new Intent(this, CreateGameActivity.class);
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
                        .GetCourts(strlocation, currentSearchTerm, rad, AppDefines.GOOGLE_API)
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
                                String testing = courtsSearchResults.courts.get(1).id;

                                Toast.makeText(getApplicationContext(), "TEST TEST",
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



