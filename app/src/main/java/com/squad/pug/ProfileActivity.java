package com.squad.pug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button openHome;
    ImageButton bLogout;
    TextView etUsername, etName, etSex, etAge;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etUsername = (TextView) findViewById(R.id.etUsername);
        etName = (TextView) findViewById(R.id.etName);
        etSex = (TextView) findViewById(R.id.etSex);
        etAge = (TextView) findViewById(R.id.etAge);
        bLogout = (ImageButton) findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);

        // Baby image, later to be profile picture
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.baby);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayUserDetails();
        //if (authenticate() == true) {
          //  displayUserDetails();
        //}else{
          //  startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        //}
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();

        etUsername.setText(user.username);
        etName.setText(user.name);
        etSex.setText(user.sex);
        etAge.setText(user.age + "");

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);



                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
