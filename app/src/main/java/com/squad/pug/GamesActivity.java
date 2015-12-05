package com.squad.pug;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class GamesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        Game game = getIntent().getParcelableExtra("GameData");

        // Fill this in later when we implement it to grab bitmap from court
        // To contain bitmap passed in from CourtActivity
        ImageView courtPhoto = (ImageView) findViewById(R.id.courtPhoto);
        Bitmap courtPhotoBitmap = getIntent().getParcelableExtra("CourtBitmap");
        courtPhoto.setImageBitmap(courtPhotoBitmap);




        // Instantiate textviews
        TextView gameCreator = (TextView) findViewById(R.id.gameCreator);
        TextView timeCreated = (TextView) findViewById(R.id.timeCreated);
        TextView dateCreated = (TextView) findViewById(R.id.dateCreated);
        TextView numPlayers = (TextView) findViewById(R.id.numPlayers);



        gameCreator.setText(game.user + "'s Game");
        timeCreated.setText("Time created: " + game.time);
        dateCreated.setText("Date created: " + game.date);
        numPlayers.setText("Number of players checked in: " +
                String.valueOf(game.num_players));


    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_games, menu);
    //    return true;
    //}

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
