package com.squad.pug;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

public class CourtActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    protected GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;
    private static final String DIALOG_ERROR = "diaglog_error";
    private static final int REQUEST_RESOLVE_ERROR = 1001;


    public ArrayList<String> getItemModelDataStringArray(){
        Intent intent = getIntent();
//        ArrayList<String> model = intent.getStringArrayListExtra("CourtData");
        ArrayList<String> model = intent.getStringArrayListExtra("Mock court");
        return model;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_court);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApiIfAvailable(LocationServices.API)
                .addApiIfAvailable(Places.GEO_DATA_API)
                .addApiIfAvailable(Places.PLACE_DETECTION_API)
                .build();
        //      getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        ArrayList<String> model = intent.getStringArrayListExtra("CourtData");
        //ArrayList<String> model = intent.getStringArrayListExtra("Mock court");

        TextView courtName = (TextView) findViewById(R.id.courtName);
        TextView courtId = (TextView) findViewById(R.id.courtId);

        // ** idkText is just the name place, I think we should make it clickable and maybe say "Directions to Here"
        // because the name of the court (item model) is already displayed at the top of the activity anyways - Trevor
        TextView idkText = (TextView) findViewById(R.id.idkTextView);
        // Indexes:
        // 0: geometry
        // 1: icon
        // 2: id
        // 3: name
        // 4: placeId
        courtName.setText(model.get(3));
        courtId.setText(model.get(2));




        ImageView courtPhoto = (ImageView) findViewById(R.id.courtPhoto);

        // COMMENTING ALL THIS OUT CUZ IM TRYNA IMPLEMENT ACTUAL PHOTO - TREVOR
        addPhoto(model, courtPhoto, idkText);

//       new ImageLoadTask(model.get(1), courtPhoto);
//       new ImageLoadTask(model.get(1), courtPhoto).execute();


    }


    public void addPhoto(ArrayList<String> model, final ImageView mImageView, final TextView mText) {



        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
        new PhotoTask(350, 350, model.get(4), mGoogleApiClient) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                mImageView.setImageResource(R.drawable.baby);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {

                    // Setting size restrictions and parameters on ImageView
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    params.height += 650;
                    params.width += 650;
                    mImageView.setLayoutParams(params);

                    // Trying to center mImageView programmatically & fucking with resolutions.
//                    mImageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                            RelativeLayout.LayoutParams.WRAP_CONTENT));
//


                    // Photo has been loaded, display it.
                    mImageView.setImageBitmap(attributedPhoto.bitmap);

                    // Display the attribution as HTML content if set.
                    if (attributedPhoto.attribution == null) {
                        mText.setVisibility(View.GONE);
                    } else {
                        mText.setVisibility(View.VISIBLE);
                          mText.setText("Directions to Here");
                       // mText.setText(Html.fromHtml(attributedPhoto.attribution.toString()));
                    }

                }
            }
        }.execute();
    }

    @Override
    public void onConnectionSuspended(int cause){

    }

    @Override
    public void onConnected(Bundle connectionHint){



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

            // CRASHING HERE ***
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

    public void openCreateGameWithCourt(View view){
        Intent intent = new Intent(this, CreateGameActivity.class);

        // intent.putStringArrayListExtra("CourtData", itemModelDataStringArray);
        ArrayList<String> itemModelDataStringArray = getItemModelDataStringArray();
        intent.putStringArrayListExtra("Mock court", itemModelDataStringArray);

        startActivity(intent);
    }

}