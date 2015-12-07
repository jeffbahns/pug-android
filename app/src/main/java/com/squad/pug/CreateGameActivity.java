package com.squad.pug;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateGameActivity extends AppCompatActivity {
    TextView setTime;
    TextView setDate;
    EditText setNumPlayers;
    ImageButton submitButton;
    Spinner locationSpinner;
    String time;
    String date;
    String location;
    int numPlayers;
    UserLocalStore userLocalStore;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        submitButton = (ImageButton) findViewById(R.id.submit);
        setTime = (TextView) findViewById(R.id.setTime);
        setDate = (TextView) findViewById(R.id.setDate);
        setNumPlayers = (EditText) findViewById(R.id.setNumPlayers);
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        userLocalStore = new UserLocalStore(this);
        ArrayList<String> courtNames = new ArrayList<>();
        courtNames.add("");
        try {
            ArrayList<String> extraCourtNames;
            extraCourtNames = getIntent().getStringArrayListExtra("courtNames");
            if( extraCourtNames != null )
                courtNames = extraCourtNames;
        } catch(RuntimeException e ) {
            e.printStackTrace();
        }
        // time picker
        setTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mCurrentTime = Calendar.getInstance();
                        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mCurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker = new TimePickerDialog(CreateGameActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                setTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }, hour, minute, false);
                        mTimePicker.setTitle("Select a court time");
                        mTimePicker.show();
                    }
                }
        );

        // date picker
        setDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mCurrentDate = Calendar.getInstance();
                        int year = mCurrentDate.get(Calendar.YEAR);
                        int month = mCurrentDate.get(Calendar.MONTH);
                        int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog mDatePicker = new DatePickerDialog(CreateGameActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void  onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay ) {
                                setDate.setText( selectedMonth + " / " + selectedDay + " / " + selectedYear );
                            }
                        }, year, month, day);
                        mDatePicker.setTitle("Select a court date");
                        mDatePicker.show();
                    }
                }
        );

        // submit button
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( onSubmit() ) {
                            AlertDialog.Builder successAlert = new AlertDialog.Builder(CreateGameActivity.this);
                            successAlert.setTitle("Confirm game creation");
                            String message =    "Time            : " + time +
                                    "\nDate             : " + date +
                                    "\nNo. Players : " + numPlayers +
                                    "\nLocation      : " + location;
                            successAlert.setMessage(message);
                            successAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id ) {
                                    userLocalStore.clearGameData();
                                    User user = userLocalStore.getUsername();
                                    String uuser = user.username.toString();
                                    Game game = new Game(0, uuser, time, date, numPlayers, location);
                                    //createGame( new Game(game);
                                    userLocalStore.storeGameData(game);
                                    getID(game);

                                }
                            });
                            successAlert.setNegativeButton("Cancel", null);
                            successAlert.show();
                        } else {
                            AlertDialog failureAlert = new AlertDialog.Builder(CreateGameActivity.this).create();
                            failureAlert.setTitle("Error");
                            failureAlert.setMessage("All fields must be successfully filled");
                            failureAlert.show();
                        }
                    }
                }
        );

        //location picker ** Tried to change color. Let's see if it works - Trevor
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, courtNames);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        locationSpinner.setAdapter(adapter);

    }

    // submit button triggers this function
    private boolean onSubmit() {
        try {
            time = setTime.getText().toString();
        } catch(RuntimeException e) {
            return false;
        }
        try {
            date = setDate.getText().toString();
        } catch(RuntimeException e) {
            return false;
        }

        try {
            numPlayers = Integer.parseInt(setNumPlayers.getText().toString());
        } catch(RuntimeException e) {
            return false;
        }

        try {
            location = locationSpinner.getSelectedItem().toString();
            if( location == "")
                return false;
        } catch(RuntimeException e) {
            return false;
        }
        return true;
    }

    private void createGame(Game game){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storedGameDataInBackground(game, new GetGameCallback() {
            @Override
            public void  done(Game returnedGame) {
                startActivity(new Intent(CreateGameActivity.this, MapsActivity.class));

            }
        });

    }
    public void getID(Game game){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.getIDInBackground(game, new GetGameCallback() {
            @Override
            public void done(Game returnedGame) {
                if (returnedGame == null) {
                    createGame(userLocalStore.getGame());
                } else {
                    userLocalStore.storeID(returnedGame);
                    Game ngame = userLocalStore.getGame();
                    int id = ngame.id + 1;
                    String user = ngame.user;
                    String time = ngame.time;
                    String date = ngame.date;
                    int num_players = ngame.num_players;
                    String location = ngame.location;
                    Game game = new Game(id, user, time, date, num_players, location);
                    createGame(game);

                }
                //return returnedGame;
            }
        });
    }
}