package com.squad.pug;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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
    public ArrayList<String> model;
    UserLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        userLocalStore = new UserLocalStore(this);
        // Initialize extras from intent
        final Game game = getIntent().getParcelableExtra("GameData");
        String placeId = getIntent().getStringExtra("CourtBitmapPlaceId");
        model = getIntent().getStringArrayListExtra("CourtData");
        System.out.println(game.players_attending);
        // Fill this in later when we implement it to grab bitmap from court
        // To contain bitmap passed in from CourtActivity
        ImageView courtPhoto = (ImageView) findViewById(R.id.courtPhoto);
        // courtPhoto.setImageResource(R.drawable.sixthmanicon_courtdefault);

        // Instantiate views
        TextView gameCreator = (TextView) findViewById(R.id.gameCreator);
        TextView timeCreated = (TextView) findViewById(R.id.timeCreated);
        TextView dateCreated = (TextView) findViewById(R.id.dateCreated);
        TextView numPlayers = (TextView) findViewById(R.id.numPlayers);
        TextView playersList = (TextView) findViewById(R.id.playersList);
        playersList.setText(game.players_attending);

        // Set all text views
        gameCreator.setText(game.user + "'s Game");
        timeCreated.setText("Time : " + game.time);
        dateCreated.setText("Date : " + game.date);
        numPlayers.setText("Number of players desired : "+
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

        // Indexes: (for Court model )
            // 0: geometry
            // 1: icon
            // 2: id
            // 3: name
            // 4: placeId
            // 5: directions formatted
            // 6: String LatLng

        ImageButton joinGame = (ImageButton) findViewById(R.id.signIntoGame);
        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = userLocalStore.getLoggedInUser();
                String uuser = user.username;
                String location = model.get(3);
                int id = game.id;
                final Game game = new Game(id, location, uuser);

                AlertDialog.Builder successAlert = new AlertDialog.Builder(GamesActivity.this);
                successAlert.setTitle("Confirm join game");
                String message = "Are you sure you want to join this game?";
                successAlert.setMessage(message);
                successAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        joinIn(game);
                    }
                });
                successAlert.setNegativeButton("Cancel", null);
                successAlert.show();
            }
        });
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
    private void joinIn(Game game){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.joinedGameInBackground(game, new GetGameCallback() {
            @Override
            public void done(Game returnedGame) {
                //Whatever we want it to do after user is entered into players_attending
                //startActivity(new Intent(CreateGameActivity.this, MapsActivity.class));
            }
        });

    }
}
