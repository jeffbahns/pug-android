package com.squad.pug;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
=======
import android.support.v7.app.AppCompatActivity;
>>>>>>> branchAlaHeffe
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

<<<<<<< HEAD
import com.squad.pug.Game;
import com.squad.pug.R;

=======
>>>>>>> branchAlaHeffe
import java.util.ArrayList;

public class GamesListView extends AppCompatActivity {

    public ArrayList<Game> localGames;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list_view);
        intent = getIntent();

        // Grab local games passed in from maps activity button
        localGames = intent.getParcelableArrayListExtra("GamesData");
        final ArrayList<String> localGamesPlaceIds = intent.getStringArrayListExtra("GamesPlaceIds");
        // Instantiate listview and then set it to games
        ListView gamesLV = (ListView) findViewById(R.id.gamesListView);
        TextView tv = new TextView(GamesListView.this);
        gamesLV.addHeaderView(tv);
        final String[] gamesList_arr = new String[localGames.size()];
        for(int i = 0; i < localGames.size(); i++ ) {
            gamesList_arr[i] = localGames.get(i).date + " -- " + localGames.get(i).time;
        }
        gamesLV.setAdapter(new ArrayAdapter<String>(GamesListView.this, R.layout.simple_list_item_trevor, gamesList_arr));
        gamesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("CLICK", gamesList_arr[0]);
                Intent intent2 = new Intent(getApplicationContext(), GamesActivity.class);
<<<<<<< HEAD
                intent2.putExtra("CourtBitmapPlaceId", localGamesPlaceIds.get(position));
                //intent.putExtra("GameData", games.get(position));
                intent2.putExtra("GameData", localGames.get(position));
                startActivity(intent2);


                }

            });
=======
                intent2.putExtra("CourtBitmapPlaceId", localGamesPlaceIds.get((int)id));
                //intent.putExtra("GameData", games.get(position));
                intent2.putExtra("GameData", localGames.get((int)id));
                startActivity(intent2);


            }

        });
>>>>>>> branchAlaHeffe
    }



}
