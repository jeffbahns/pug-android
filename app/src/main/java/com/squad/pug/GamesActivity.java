package com.squad.pug;

import android.content.IntentSender;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;


public class GamesActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    protected GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;
    private static final String DIALOG_ERROR = "diaglog_error";
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    public Bitmap courtBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        // Initialize extras from intent
        Game game = getIntent().getParcelableExtra("GameData");
        String placeId = getIntent().getStringExtra("CourtBitmapPlaceId");

        // Fill this in later when we implement it to grab bitmap from court
        // To contain bitmap passed in from CourtActivity
        ImageView courtPhoto = (ImageView) findViewById(R.id.courtPhoto);
        // courtPhoto.setImageResource(R.drawable.sixthmanicon_courtdefault);

        // Instantiate views
        TextView gameCreator = (TextView) findViewById(R.id.gameCreator);
        TextView timeCreated = (TextView) findViewById(R.id.timeCreated);
        TextView dateCreated = (TextView) findViewById(R.id.dateCreated);
        TextView numPlayers = (TextView) findViewById(R.id.numPlayers);


        // Set all text views
        gameCreator.setText(game.user + "'s Game");
        timeCreated.setText("Time created: " + game.time);
        dateCreated.setText("Date created: " + game.date);
        numPlayers.setText("Number of players checked in: " +
                String.valueOf(game.num_players));

        // Build another google api client!
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApiIfAvailable(LocationServices.API)
                .addApiIfAvailable(Places.GEO_DATA_API)
                .addApiIfAvailable(Places.PLACE_DETECTION_API)
                .build();

        // Another court photo call!
        addPhoto(placeId, courtPhoto);

    }

    public void addPhoto(String placeId, final ImageView mImageView) {

        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
        // Changing res to 300x300 for GamesActivity - Trevor
        new PhotoTask(300, 300, placeId, mGoogleApiClient) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                mImageView.setImageResource(R.drawable.sixthmanicon_courtdefault);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {

                    // Setting size restrictions and parameters on ImageView
                    /*
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mImageView.getLayoutParams();
                    params.height += 650;
                    params.width += 650;
                    mImageView.setLayoutParams(params);
                    */

                    // Photo has been loaded, display it.
                    mImageView.setImageBitmap(attributedPhoto.bitmap);
                    courtBitmap = attributedPhoto.bitmap;

                    // Display the attribution as HTML content if set.
                    if (attributedPhoto.attribution == null) {
                   //     mText.setVisibility(View.GONE);
                    } else {
                   //     mText.setVisibility(View.VISIBLE);
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_games) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
