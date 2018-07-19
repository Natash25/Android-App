/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private TextView mWeatherTextView;
    //(6) Add a TextView variable for the error message display
    private TextView mErrorMessageDisplay;

    //(16) Add a ProgressBar variable to show and hide the progress bar
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_display);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // (9) Call loadWeatherData to perform the network request to get the weather
        loadWeatherData();
    }

    // (8) Create a method that will get the user's preferred location and execute new AsyncTask and call it loadWeatherData
    private void loadWeatherData() {
        showWeatherDataView(); //show weather data before doing AsyncTask
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new WeatherQueryTask().execute(location);
    }
    // (8) Create a method called showWeatherDataView that will hide the error message and show the weather data
    protected void showWeatherDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mWeatherTextView.setVisibility(View.VISIBLE);
    }


    // (9) Create a method called showErrorMessage that will hide the weather data and show the error message
    protected void showErrorMessage() {
        mWeatherTextView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    // (5) Created a class that extends AsyncTask to perform network requests
    public class WeatherQueryTask extends AsyncTask<String, Void, String[]> {
        //(18) Within your AsyncTask, override the method onPreExecute and show the loading indicator
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String ... params) { //params = zip code
            if (params.length == 0) {
                return null;
            }
            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);
            try {
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                Log.v(TAG, "got response from URL");

                // parses JSON from a web response and returns an array of Strings
                // describing the weather over various days from the forecast.
                String[] simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
                Log.v(TAG, "parsed JSON"); //TODO: cant get past getting a jsonWeatherResponse from URL!!!
                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            // (19) As soon as the data is finished loading, hide the loading indicator
            mProgressBar.setVisibility(View.INVISIBLE);

            if (weatherData != null) {
                //(11) If the weather data was not null, make sure the data view is visible
                showWeatherDataView();

                for (String weatherString : weatherData) {
                    mWeatherTextView.append(weatherString + "\n\n\n");
                }
            }
            //(10) If the weather data was null, show the error message
            else {
                showErrorMessage();
            }

        }
    }
    // (6) Override the doInBackground method to perform network requests
    // (7) Override the onPostExecute method to display the results of the network request

    // (2) Create a menu resource in res/menu/ called forecast.xml
    // (3) Add one item to the menu with an ID of action_refresh
    // (4) Set the title of the menu item to "Refresh" using strings.xml

    // (5) Override onCreateOptionsMenu to inflate the menu for this Activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return true;
    }
    //(6) Return true to display the menu

    //(7) Override onOptionsItemSelected to handle clicks on the refresh button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            mWeatherTextView.setText("");
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}