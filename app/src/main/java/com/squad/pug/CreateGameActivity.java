package com.squad.pug;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import static com.squad.pug.R.id.submit;


/*
**********************************************************
* ~~SO I DON'T FORGET~~
* NOTE FROM TREVOR ----> JEFF:
* This will fail if it is called (using the + button in the app) with no searchresultmodel object.
* We'll later implement a check to see if they've populated their result model yet.
*
* *********************************************************
 */



public class CreateGameActivity extends AppCompatActivity {
    TextView setTime;
    TextView setDate;
    AutoCompleteTextView autoLocation;
    EditText setNumPlayers;
    EditText setLocation;
    Button submitButton;
    String time;
    String date;
    String location;
    int numPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        submitButton = (Button) findViewById(submit);
        setTime = (TextView) findViewById(R.id.setTime);
        setDate = (TextView) findViewById(R.id.setDate);
        setNumPlayers = (EditText) findViewById(R.id.setNumPlayers);
        ArrayList<String> courtNames = getIntent().getStringArrayListExtra("courtNames");
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

        autoLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    location = (String)parent.getItemAtPosition(position);
                                                }
                                            }
        );

                // autocomplete location picker
                String[] COURTS = new String[] {
                "Lady Bug Park", "Dorotea Park", "Callinan Sports & Fitness Center",
                "Rancho Cotate High School", "Sonoma State University"
        };
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoLocation);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, courtNames);
        textView.setAdapter(adapter);

        // submit button
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String error = "";
                        try {
                            time = setTime.getText().toString();
                        } finally {
                            if( time == "")
                                error += "Time not entered\n";
                        }

                        try {
                            date = setDate.getText().toString();
                        } finally {
                            if( date == "")
                                error += "Date not entered\n";
                        }

                        try {
                            numPlayers = Integer.parseInt(setNumPlayers.getText().toString());
                        } catch (RuntimeException e) {
                            error += "Number of players not entered\n";
                        }

                        try {
                            location = location;
                        } catch(RuntimeException e) {
                            error += "Location not entered\n";
                        }
                        if( error != "")
                            System.out.println(error);

                        /*
                        Toast courtSnippet = Toast.makeText(CreateGameActivity.this, error, Toast.LENGTH_SHORT);
                        courtSnippet.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        courtSnippet.show();
                        //testPrint();
                        */
                        AlertDialog alertDialog = new AlertDialog.Builder(CreateGameActivity.this).create();
                        alertDialog.setTitle("Hello");
                        alertDialog.setMessage(error);

                        alertDialog.show();

                    }
                }
        );


    }


    protected void testPrint() {
        Log.v("Time : ", time);
        Log.v("Date : ", date);
        Log.v("Number Players : ", String.valueOf(numPlayers));
        Log.v("Location : ", location);
    }
}