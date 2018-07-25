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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.ForecastAdapter;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.io.IOException;
import java.net.URL;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.EXTRA_TEXT;
// (8) Implement ForecastAdapterOnClickHandler from the MainActivity

public class MainActivity extends AppCompatActivity implements ForecastAdapter.ForecastAdapterOnClickHandler {
    // Within forecast_list_item.xml //////////////////////////////////////////////////////////////
    // (5) Add a layout for an item in the list called forecast_list_item.xml
    // (6) Make the root of the layout a vertical LinearLayout
    // (7) Set the width of the LinearLayout to match_parent and the height to wrap_content

    // (8) Add a TextView with an id @+id/tv_weather_data
    // (12) Add a View to the layout with a width of match_parent and a height of 1dp

    // Within forecast_list_item.xml //////////////////////////////////////////////////////////////


    // Within ForecastAdapter.java /////////////////////////////////////////////////////////////////
    // (15) Add a class file called ForecastAdapter
    // (22) Extend RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>

    // (23) Create a private string array called mWeatherData

    // (16) Create a class within ForecastAdapter called ForecastAdapterViewHolder
    // (17) Extend RecyclerView.ViewHolder

    // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////
    // (18) Create a public final TextView variable called mWeatherTextView

    // (19) Create a constructor for this class that accepts a View as a parameter
    // (20) Call super(view) within the constructor for ForecastAdapterViewHolder
    // (21) Using view.findViewById, get a reference to this layout's TextView and save it to mWeatherTextView
    // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////


    // (24) Override onCreateViewHolder
    // (25) Within onCreateViewHolder, inflate the list item xml into a view
    // (26) Within onCreateViewHolder, return a new ForecastAdapterViewHolder with the above view passed in as a parameter

    // (27) Override onBindViewHolder
    // (28) Set the text of the TextView to the weather for this list item's position

    // (29) Override getItemCount

    // (31) Create a setWeatherData method that saves the weatherData to mWeatherData
    // (32) After you save mWeatherData, call notifyDataSetChanged
    // Within ForecastAdapter.java /////////////////////////////////////////////////////////////////


    private static final String TAG = MainActivity.class.getSimpleName();

    // (34) Add a private RecyclerView variable called mRecyclerView
    private RecyclerView mRecyclerView;
    // (35) Add a private ForecastAdapter variable called mForecastAdapter
    private ForecastAdapter mForecastAdapter;
    //(6) Add a TextView variable for the error message display
    private TextView mErrorMessageDisplay;

    //(16) Add a ProgressBar variable to show and hide the progress bar
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        // (37) Use findViewById to get a reference to the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);

        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_display);
        // (38) Create layoutManager, a LinearLayoutManager with VERTICAL orientation and shouldReverseLayout == false
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // (39) Set the layoutManager on mRecyclerView
        mRecyclerView.setLayoutManager(layoutManager);

        // (40) Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list will have the same size
        mRecyclerView.setHasFixedSize(true);
        // (11) Pass in 'this' as the ForecastAdapterOnClickHandler
        mForecastAdapter = new ForecastAdapter(this);


        // (42) Use mRecyclerView.setAdapter and pass in mForecastAdapter
        mRecyclerView.setAdapter(mForecastAdapter);

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
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    // (9) Create a method called showErrorMessage that will hide the weather data and show the error message
    protected void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    // (10) Show a Toast when an item is clicked, displaying that item's weather data
    @Override
    public void onClick(String weatherForToday) {
        Context context = this;
        // (1) Create a new Activity called DetailActivity using Android Studio's wizard
        // (2) Change the root layout of activity_detail.xml to a FrameLayout and remove unnecessary xml attributes
        // (3) Remove the Toast and launch the DetailActivity using an explicit Intent
        Class destinationClass = DetailActivity.class;
        Intent intent = new Intent(context, destinationClass);
        // (1) Pass the weather to the DetailActivity
        intent.putExtra(EXTRA_TEXT, weatherForToday);
        startActivity(intent);
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
                Log.v(TAG, "parsed JSON");
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
                //(45) Instead of iterating through every string, use mForecastAdapter.setWeatherData and pass in the weather data
                mForecastAdapter.setWeatherData(weatherData);
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
            // (46) Instead of setting the text to "", set the adapter to null before refreshing
            mForecastAdapter.setWeatherData(null);
            loadWeatherData();
            return true;
        }

        // (2) Launch the map when the map menu item is clicked
        if (id == R.id.action_map) {
            openMap();
        }
        return super.onOptionsItemSelected(item);
    }

    public void openMap() {
        String addressString = "1521 7th Avenue";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("geo")
                .path("0,0")
                .query(addressString);
        Uri addressUri = builder.build();
        Intent intent = new Intent(ACTION_VIEW);
        intent.setData(addressUri);
        if (intent.resolveActivity(getPackageManager())!=null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + addressUri.toString()
                    + ", no receiving apps installed!");
        }
    }
}