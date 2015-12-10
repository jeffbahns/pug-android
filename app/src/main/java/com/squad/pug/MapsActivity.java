package com.squad.pug;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.squad.pug.models.LocationData;
import com.squad.pug.models.SearchItemModel;
import com.squad.pug.models.SearchResultModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;


public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    public SearchResultModel courtsResult;
    public HashMap<String, SearchItemModel> markerMap = new HashMap<String, SearchItemModel>();
    //public android.app.Fragment fragment = getFragmentManager().findFragmentById(R.id.FraggyList);
    protected GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;
    private static final String DIALOG_ERROR = "diaglog_error";
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private boolean alreadyUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Build another google api client!
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApiIfAvailable(LocationServices.API)
                .addApiIfAvailable(Places.GEO_DATA_API)
                .addApiIfAvailable(Places.PLACE_DETECTION_API)
                .build();

        ImageButton findLocalGames = (ImageButton) findViewById(R.id.find_games);
        findLocalGames.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            final Location myLocation = mMap.getMyLocation();
                            final String locationLatLong = String.valueOf(myLocation.getLatitude()) + "," + String.valueOf(myLocation.getLongitude());
                            populateMap(locationLatLong);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );




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
        String locationLatLong = "";
        String locationLat = "";
        String locationLng = "";
        try {
            final Location myLocation = mMap.getMyLocation();

            // Trevor's testing shit for updating map when app is started
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                locationLat = (String.valueOf(mLastLocation.getLatitude()));
                locationLng = (String.valueOf(mLastLocation.getLongitude()));
                locationLatLong = locationLat + "," + locationLng;

            if (alreadyUpdated == false) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngFormatter(locationLatLong), 12));
                alreadyUpdated = true;
            }

        }} catch (RuntimeException e) {
            e.printStackTrace();
        }



    }

    public void populateMap(final String location) throws IOException {

        new AsyncTask<String, String, SearchResultModel>(){
            @Override
            protected SearchResultModel doInBackground(String... params) {
                SearchResultModel model = new SearchResultModel();

                try {
                    System.out.println("Getting courts..");

                    // Instantiate Gson
                    Gson gson = new Gson();

                    // Building query string
                    String rad = "&radius=500";
                    String type = "&keyword=basketball+court";
                    String query = "&query=basketball+court";

                    // Make HTTP Connection & Request
                    String urlString = AppDefines.urlStringBaseText + location + rad + query + "&key=" + AppDefines.GOOGLE_SERVER_API;
                    URL url = new URL(urlString);
                    URLConnection conn = url.openConnection();
                    InputStream is = conn.getInputStream();

                    // Deserialize data into SearchResultModel, which is an ArrayList<SearchItemModel>!
                    Reader reader = new InputStreamReader(is);
                    model = gson.fromJson(reader, SearchResultModel.class);
                    courtsResult = model;


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
                final Context mContext = MapsActivity.this.getApplicationContext();
                result.grabGamesFromDatabaseForEachCourt(mContext, mMap, markerMap);
        //        result.populateMapWithModels(mMap, mContext, markerMap);
                // Move camera to current testLocation, but eventually to my current location / last known location!
                try {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngFormatter(location), 12));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngFormatter(location)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                    System.out.println("COURT REQUEST FAILED");
                }
//
                // console print test
                //result.print();
            }
        }.execute("");
        onPostPopulate();
    }

    public void onPostPopulate() {

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(MapsActivity.this, CourtActivity.class);

                // looks into hashmap for correct marker, then grabs the array
                intent.putExtra("CourtData", markerMap.get(marker.getId()).getItemModelStringArray());
                intent.putParcelableArrayListExtra("GameData", markerMap.get(marker.getId()).gamesList);
                markerMap.get(marker.getId()).arrayIterator(markerMap.get(marker.getId()).getItemModelStringArray());
                startActivity(intent);

                System.out.println("** Marker Clicked **");
                return true;
            }
        });


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
        ArrayList<Game> localGames = new ArrayList<>();
        ArrayList<String> gamesPlaceIds = new ArrayList<>();
        try {
            localGames = courtsResult.grabGames();
            gamesPlaceIds = courtsResult.getArrayOfPlaceIds();
        } catch(RuntimeException e){
            e.printStackTrace();
        }

        Intent intent = new Intent(this, GamesListView.class);
        intent.putParcelableArrayListExtra("GamesData", localGames);
        intent.putStringArrayListExtra("GamesPlaceIds", gamesPlaceIds);

        startActivity(intent);
    }

    public void openCreateGame(View view) {
        Intent intent = new Intent(this, CreateGameActivity.class);
        try {
            intent.putExtra("courtNames", courtsResult.getArrayOfNames());
        } catch( RuntimeException e) {
            e.printStackTrace();
        }
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


    // **** HELPER FUNCTIONS ****
    //
    //

    // this takes a coordinate string (API call string), changes it to latLng object
    // took this from app defines so we could reuse
    public LatLng latLngFormatter(String strLatLng) {
        final String[] latlong = strLatLng.split(",");
        final double latitude = Double.parseDouble(latlong[0]);
        final double longitude = Double.parseDouble(latlong[1]);
        return new LatLng(latitude, longitude);
    }



// BANISHED CODE //
// DONT NEED THIS ANYMORE, JUST PUTTING IT HERE FOR REFERENCE BECAUSE IT WORKS //

/* 1 */
// Toast works 100%, probably a place holder until pop up fragment is functioning
//Toast courtSnippet = Toast.makeText(MapsActivity.this, markerMap.get(marker.getId()).name, Toast.LENGTH_SHORT);
//courtSnippet.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
//courtSnippet.show();

    // ** Called after Place Picker location selected
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place p = PlacePicker.getPlace(data, this);
                LatLng loc = p.getLatLng();
                // Update camera after set search location has been pressed
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p.getLatLng(), 12));
                System.out.println(loc.toString());
                LocationData locationData = new LocationData(loc);
                String strloclat = locationData.getLat().toString();
                String strloclng = locationData.getLng().toString();
                String strlocation = strloclat + "," + strloclng;
                try {
                    populateMap(strlocation);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onConnectionSuspended(int cause){

    }

    @Override
    public void onConnected(Bundle connectionHint){

        String locationLatLong = "";
        String locationLat = "";
        String locationLng = "";

        try {
            final Location myLocation = mMap.getMyLocation();

            // Trevor's testing shit for updating map when app is started
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                locationLat = (String.valueOf(mLastLocation.getLatitude()));
                locationLng = (String.valueOf(mLastLocation.getLongitude()));
                locationLatLong = locationLat + "," + locationLng;

                //   locationLatLong = String.valueOf(myLocation.getLatitude()) + "," + String.valueOf(myLocation.getLongitude());
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngFormatter(locationLatLong), 12));
                System.out.println("LOCATION STRING:" + locationLatLong);
                //  mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngFormatter(locationLatLong)));
            }} catch (RuntimeException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
        if (mResolvingError){
            return;
        } else if (result.hasResolution()) {
            try{
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            }
            catch (IntentSender.SendIntentException e){
                mGoogleApiClient.connect();
            }
        }
        else{
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }

    }

    @Override
    protected void onStart(){
        super.onStart();
        if (!mResolvingError) {

            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void showErrorDialog(int errorCode){
        Launcher.ErrorDialogFragment dialogFragment = new Launcher.ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }



}
