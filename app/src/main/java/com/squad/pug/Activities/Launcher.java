package com.squad.pug.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.squad.pug.R;


public class Launcher extends AppCompatActivity
        implements ConnectionCallbacks, OnConnectionFailedListener {

    public final static String EXTRA_MSG = "squad.mapsapptest.MESSAGE";
    //    protected TextView mLatitudeText = new TextView(this);
//    protected TextView mLongitudeText = new TextView(this);
    protected GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;
    private static final String DIALOG_ERROR = "diaglog_error";
    private static final int REQUEST_RESOLVE_ERROR = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // Build following Google APIs: Location Servcices; Places[GeoDataApi & PlaceDetectionApi]
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApiIfAvailable(LocationServices.API)
                .addApiIfAvailable(Places.GEO_DATA_API)
                .addApiIfAvailable(Places.PLACE_DETECTION_API)
                .build();
    }

    @Override
    public void onConnectionSuspended(int cause){

    }

    @Override
    public void onConnected(Bundle connectionHint){
        TextView mLatitudeText = new TextView(this);
        TextView mLongitudeText = new TextView(this);

        // Request Last Location
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        /*
      //  if (mLastLocation != null){
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        */
        mLatitudeText.setText(String.valueOf(-122.084095));
        mLongitudeText.setText(String.valueOf(37.422006));
        //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
        //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

       /*     // Do something to notify user to enable location services in their device settings
        }  else if (mLastLocation == null) {
            Intent intentnull = new Intent(this, ProfileActivity    .class);
            String message = "Please enable your location services in settings.";
            intentnull.putExtra(EXTRA_MSG, message);
            startActivity(intentnull);
        }


        Intent intent = new Intent(this, MapsActivity.class);
        String message = "You have connected to Google!";
        intent.putExtra(EXTRA_MSG, message);
        startActivity(intent);*/

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
        if (!mResolvingError) {  // more about this later
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }



    private void showErrorDialog(int errorCode){
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    public void onDialogDismissed(){
        mResolvingError = false;
    }





    public static class ErrorDialogFragment extends DialogFragment{
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((Launcher) getActivity()).onDialogDismissed();
        }
    }
}
