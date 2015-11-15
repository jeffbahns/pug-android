package com.squad.pug.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squad.pug.R;

import java.util.Calendar;

import static com.squad.pug.R.id.submit;

public class CreateGameActivity extends AppCompatActivity {
    TextView setTime;
    TextView setDate;
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
                        time = setTime.getText().toString();
                        date = setDate.getText().toString();
                        numPlayers = Integer.parseInt(setNumPlayers.getText().toString());
                        //location = setLocation.getText().toString();

                        testPrint();
                        /*
                        AlertDialog alertDialog = new AlertDialog.Builder(CreateGameActivity.this).create();
                        alertDialog.setTitle("Hello");
                        alertDialog.setMessage("Your game was successfully created");
                        alertDialog.show();
                        */
                    }
                }
        );

        // autocomplete location picker
        String[] COURTS = new String[] {
                "Lady Bug Park", "Dorotea Park", "Callinan Sports & Fitness Center",
                "Rancho Cotate High School", "Sonoma State University"
        };
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoLocation);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, COURTS);
        textView.setAdapter(adapter);
    }

    protected void testPrint() {
        Log.v("Time : ", time);
        Log.v("Date : ", date);
        Log.v("Number of Players Desired : ", String.valueOf(numPlayers));
    }
}
