/*
 * Declaring the package
 */
package com.example.weather_app;
/*
 * Importing all the required Android Libraries
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MoreInfo extends AppCompatActivity implements View.OnClickListener {
    /*
     * Declaring private class variables
     */
    Button btn1;
    Button btn2;
    ImageView bg;
    SharedPreferences mySharedPref;
    private static final String Shared_Pref_Name = "myPrefs";
    private static final String Key_Bg = "bgColour";
    /*
    *  Initialises some variables, and checks for saved preferences before applying them
    * if found. It also sets click listeners for two buttons
    */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /*
         * Perform the default initialisation of the activity
         */
        super.onCreate(savedInstanceState);
        /*
         * Sets the layout for the current activity to the XML layout file activity_main.xml
         */
        setContentView(R.layout.activity_moreinfo);
        /*
         * Initialising variables
         */
        bg = findViewById(R.id.idMoreInfoBackgroundColour);
        btn1 = (Button) findViewById(R.id.idLink);
        btn2 = (Button) findViewById(R.id.idBack2);
        /*
         * Retrieves the shared preferences associated with the activity
         */
        mySharedPref = getSharedPreferences(Shared_Pref_Name, MODE_PRIVATE);
        if (mySharedPref != null && mySharedPref.contains(Key_Bg)) {
            applySavedPrefs();
        } else {
            Toast.makeText(getApplicationContext(), "No Preferences Found!",
                    Toast.LENGTH_SHORT).show();
        }
        /*
         * Sets click listeners on four buttons in the activity_bg layout
         */
        btn1.setOnClickListener((View.OnClickListener) this);
        btn2.setOnClickListener((View.OnClickListener) this);
    }
    /*
     * Responsible for applying the user's previously saved background color preference to the
     * app's background which is saved in the SharedPreferences
     */
    public void applySavedPrefs() {
        int bgColour = mySharedPref.getInt(Key_Bg, R.drawable.bg_gradient);
        bg.setImageResource(bgColour);
    }
    /*
    * An implementation of onClick listener for two buttons in an Android app, where one
    * opens a website and the other goes back to the main activity
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idLink:
                /*
                * Uses an implicit intent to communicate with another application when button 1 is
                * clicked
                */
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" +
                        "www.weatherapi.com/weather/q/exeter-devon-united-kingdom-2791087"));
                startActivity(intent1);
                break;
            /*
             * Uses an explicit intent to return the user to activity_main when button 2 is clicked
             */
            case R.id.idBack2:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;
        }
    }
}


