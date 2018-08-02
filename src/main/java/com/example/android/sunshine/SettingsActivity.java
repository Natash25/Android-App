package com.example.android.sunshine;

import android.annotation.SuppressLint;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar !=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // (2) Create an xml resource directory
        // (3) Add a PreferenceScreen with an EditTextPreference and ListPreference within the newly created xml resource directory

        // (4) Create SettingsFragment and extend PreferenceFragmentCompat

        // Do steps 5 - 11 within SettingsFragment
        // (10) Implement OnSharedPreferenceChangeListener from SettingsFragment

        // (8) Create a method called setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference

        // (5) Override onCreatePreferences and add the preference xml file using addPreferencesFromResource

        // Do step 9 within onCreatePreference
        // (9) Set the preference summary on each preference that isn't a CheckBoxPreference

        // (13) Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop

        // (12) Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart

        // (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
