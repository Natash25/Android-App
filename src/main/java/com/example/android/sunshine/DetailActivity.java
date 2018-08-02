package com.example.android.sunshine;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static android.content.Intent.EXTRA_TEXT;

public class DetailActivity extends AppCompatActivity {
    private TextView mDisplayText;
    private String textOfWeather;
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mDisplayText = (TextView) findViewById(R.id.tv_weather_display);

        Intent intentFromMain = getIntent();
        if (intentFromMain!=null) {
            if (intentFromMain.hasExtra(EXTRA_TEXT)) {
                textOfWeather = intentFromMain.getStringExtra(EXTRA_TEXT);
                mDisplayText.setText(textOfWeather);
            }
        }
    }
    // (3) Create a menu with an item with id of action_share
    // (4) Display the menu and implement the forecast sharing functionality
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(textOfWeather + FORECAST_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }
    //(7) Launch SettingsActivity when the Settings option is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings_intent = new Intent(this, SettingsActivity.class);
            startActivity(settings_intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
