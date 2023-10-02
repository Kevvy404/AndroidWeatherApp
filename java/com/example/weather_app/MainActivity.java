/*
* Declaring the package
*/
package com.example.weather_app;
/*
* Importing all the required Android Libraries
*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
     /*
     * Declaring private class variables
     */
    private RelativeLayout home;
    private ProgressBar progressBar;
    private TextView cityName, countryName, temp, condition;
    private RecyclerView weather;
    private TextInputEditText cityEdit;
    private ImageView icon, searchIcon;
    private ArrayList<WeatherModal> weatherModalArrayList;
    private WeatherAdapter weatherAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String userCityName;
    private Toolbar idToolBar;
    private static final String Shared_Pref_Name = "myPrefs";
    private static final String Key_Bg = "bgColour";
    ImageView view;
    SharedPreferences mySharedPref;
    SharedPreferences.Editor editor;
    /*
    * Initialises the activity, sets up the user interface and layout, requests permissions,
    * retrieves and displays weather information, and sets up event listeners for user interaction.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        * Perform the default initialisation of the activity
        */
        super.onCreate(savedInstanceState);
        /*
        * Sets the layout for the current activity to the XML layout file activity_main.xml
        */
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        /*
        * Initialising variables
        */
        home = findViewById(R.id.idHome);
        progressBar = findViewById(R.id.idProgressBar);
        cityName = findViewById(R.id.idCityName);
        countryName = findViewById(R.id.idCountryName);
        temp = findViewById(R.id.idTemp);
        condition = findViewById(R.id.idCondition);
        weather = findViewById(R.id.idRVWeather);
        cityEdit = findViewById(R.id.idCityEdit);
        icon = findViewById(R.id.idImageIcon);
        searchIcon = findViewById(R.id.idImageSearch);
        idToolBar = findViewById(R.id.idToolBar);
        view = findViewById(R.id.idBackgroundColour);
        weatherModalArrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this, weatherModalArrayList);
        weather.setAdapter(weatherAdapter);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /*
        * Retrieves the shared preferences associated with the activity
        */
        mySharedPref = getSharedPreferences(Shared_Pref_Name, MODE_PRIVATE);
        editor = mySharedPref.edit();

        if (mySharedPref != null && mySharedPref.contains(Key_Bg)) {
            applySavedPrefs();
        } else {
            Toast.makeText(getApplicationContext(), "No Preferences Found!",
                    Toast.LENGTH_SHORT).show();
        }
        /*
        * Checks if the app has been granted the necessary permissions to access the device's
        * location
        */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.
                        ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.
                            ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
        /*
        * Tries to retrieve the user's last known location
        */
        try {
            Location location = locationManager.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER);
            userCityName = getCityName(location.getLongitude(), location.getLatitude());
            getWeatherInformation(userCityName);
        } catch (Exception e) {
        }
        /*
        * Sets up the toolbar as the default app bar for this activity
        */
        idToolBar.setTitle("");
        setSupportActionBar(idToolBar);
        /*
        * Sets up the functionality to allow users to search for weather information for
        * a specific city
        */
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityEdit.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a city name",
                            Toast.LENGTH_SHORT).show();
                } else {
                    cityName.setText(userCityName);
                    getWeatherInformation(city);
                }
            }
        });
    }
    /*
    * Responsible for applying the user's previously saved background color preference to the
    * app's background which is saved in the SharedPreferences
    */
    public void applySavedPrefs() {
        int bgColour = mySharedPref.getInt(Key_Bg, R.drawable.bg_gradient);
        view.setImageResource(bgColour);
    }
    /*
    * Inflates the menu layout and populates the menu with menu items
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    /*
    * Responsible for handling menu item selections
    */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        editor.clear();
        String pref = "Preferences Saved!";

        switch (item.getItemId()) {
            /*
             * Sets the userCityName to one of the preset cities clicked, such as Exeter, and gets
             * the weather information for that city
             */
            case R.id.idExeter:
                userCityName = "Exeter";
                getWeatherInformation(userCityName);
                return true;

            case R.id.idLondon:
                userCityName = "London";
                getWeatherInformation(userCityName);
                return true;

            case R.id.idParis:
                userCityName = "Paris";
                getWeatherInformation(userCityName);
                return true;

            case R.id.idShanghai:
                userCityName = "Shanghai";
                getWeatherInformation(userCityName);
                return true;

            case R.id.idNewYork:
                userCityName = "New York";
                getWeatherInformation(userCityName);
                return true;
            /*
            * Sets the background to one of the presets when clicked and is then saved to the
            * SharedPreference file. A toast then pops up saying that the preference has been saved
            */
            case R.id.idBg1:
                view.setImageResource(R.drawable.bg_gradient);
                editor.putInt("bgColour", R.drawable.bg_gradient);
                editor.commit();
                applySavedPrefs();
                Toast.makeText(MainActivity.this, pref,
                        Toast.LENGTH_SHORT).show();
                return true;

            case R.id.idBg2:
                view.setImageResource(R.drawable.bg_gradient2);
                editor.putInt("bgColour", R.drawable.bg_gradient2);
                editor.commit();
                applySavedPrefs();
                Toast.makeText(MainActivity.this, pref,
                        Toast.LENGTH_SHORT).show();
                return true;

            case R.id.idBg3:
                view.setImageResource(R.drawable.bg_gradient3);
                editor.putInt("bgColour", R.drawable.bg_gradient3);
                editor.commit();
                applySavedPrefs();
                Toast.makeText(MainActivity.this, pref,
                        Toast.LENGTH_SHORT).show();
                return true;

            case R.id.idBg4:
                view.setImageResource(R.drawable.bg_gradient4);
                editor.putInt("bgColour", R.drawable.bg_gradient4);
                editor.commit();
                applySavedPrefs();
                Toast.makeText(MainActivity.this, pref,
                        Toast.LENGTH_SHORT).show();
                return true;
            /*
             * Takes the user to activity_moreinfo when button 2 in the menu is clicked
             */
            case R.id.idMoreInfo:
                Intent intent3 = new Intent(this, MoreInfo.class);
                startActivity(intent3);
                return true;
            /*
             * Refreshes the app when button 3 in the menu is clicked
             */
            case R.id.idRefreshApp:
                finish();
                startActivity(getIntent());
            /*
             * Closes the app when button 4 in the menu is clicked
             */
            case R.id.idExitApp:
                finish();
                System.exit(0);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*
    * Checks whether the locationpermissions asked are granted or not
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    /*
    * Takes in the longitude and latitude of a location, uses a Geocoder to get the city name
    * of that location and returns it as a string
    */
    private String getCityName(double longitude, double latitude) {
        String userCityName = "City Not Found!";
        Geocoder geo = new Geocoder(getBaseContext(), Locale.getDefault());
        /*
        * Uses the latitude and longitude values to retrieve a list of possible addresses,
        * and then checks each address to obtain the name of the city to which it belongs
        */
        try {
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 10);

            for (Address address : addresses) {
                if (address != null) {
                    String city = address.getLocality();
                    if (city != null && !city.equals("")) {
                        userCityName = city;
                    } else {
                        Log.d("TAG", "City Not Found!");
                        Toast.makeText(this, "City Not Found!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userCityName;
    }
    /*
    * Retrieves the weather information about the queried city
    */
    private void getWeatherInformation(String userCityName) {
        /*
        * Alters the API by using the city the user entered into the query box
        * to then return the information about the queried city
        */
        String url = "http://api.weatherapi.com/v1/forecast.json?key=" +
                "5c8d353ab13b43e892e143357231502&q=" + userCityName + "&days=1&aqi=no&alerts=no";
        cityName.setText(userCityName);
        RequestQueue reqQ = Volley.newRequestQueue(MainActivity.this);
        /*
        * Creates a new JSON Object request using GET method with a specified URL and a response
        * listener to be triggered upon successful response from the server
        */
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                home.setVisibility(View.VISIBLE);
                weatherModalArrayList.clear();
                /*
                * extracts various weather-related data from the JSON response object returned
                * by the weather API and populates the relevant fields in the UI with that data
                */
                try {
                    String userCountryName = response.getJSONObject("location").
                            getString("country");

                    countryName.setText(userCountryName);

                    String temperature = response.getJSONObject("current").
                            getString("temp_c");

                    temp.setText(temperature + "°C");

                    int isDay = response.getJSONObject("current").
                            getInt("is_day");

                    String currentCondition = response.getJSONObject("current").
                            getJSONObject("condition").getString("text");

                    String conditionIcon = response.getJSONObject("current").
                            getJSONObject("condition").getString("icon");

                    Picasso.get().load("http:".concat(conditionIcon)).into(icon);
                    condition.setText(currentCondition);

                    JSONObject forecast = response.getJSONObject("forecast");

                    JSONObject forecastDay = forecast.getJSONArray("forecastday").
                            getJSONObject(0);

                    JSONArray hour = forecastDay.getJSONArray("hour");
                    /*
                    * iterates over each hour in the hour JSONArray, extracts the time,
                    *  temperature, icon and wind data for each hour and creates a new
                    *  WeatherModal object with this data. These WeatherModal objects are
                    *  then added to the weatherModalArrayList
                    */
                    for (int i = 0; i < hour.length(); i++) {
                        JSONObject hourObject = hour.getJSONObject(i);
                        String time = hourObject.getString("time");
                        String temperature2 = hourObject.getString("temp_c");
                        String iconn = hourObject.getJSONObject("condition").
                                getString("icon");
                        String wind = hourObject.getString("wind_mph");
                        weatherModalArrayList.add(new WeatherModal(time, temperature2,
                                iconn, wind));
                    }

                    weatherAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            /*
            * Handles the case where the API call returns an error
            */
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Enter valid city name",
                        Toast.LENGTH_SHORT).show();
            }
        });
        reqQ.add(jsonObjectRequest);
    }
}

