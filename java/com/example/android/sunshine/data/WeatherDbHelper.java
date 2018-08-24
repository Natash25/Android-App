package com.example.android.sunshine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for weather data.
 */
// (11) Extend SQLiteOpenHelper from WeatherDbHelper
public class WeatherDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "weather.db";

    //  (2) Increment the database version after altering the behavior of the table
     //* If you change the database schema, you must increment the database version or the onUpgrade
     //* method will not be called.
    public static final int DATABASE_VERSION = 3;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // this string will be used the execute the SQL statement on the SQLite object

        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + WeatherContract.WeatherEntry.TABLE_NAME + " (" +

                        /*
                         * WeatherEntry did not explicitly declare a column called "_ID". However,
                         * WeatherEntry implements the interface, "BaseColumns", which does have a field
                         * named "_ID". We use that here to designate our table's primary key.
                         */
                        WeatherContract.WeatherEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        WeatherContract.WeatherEntry.COLUMN_DATE       + " INTEGER NOT NULL, "                 +

                        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL,"                  +

                        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP   + " REAL NOT NULL, "                    +
                        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP   + " REAL NOT NULL, "                    +

                        WeatherContract.WeatherEntry.COLUMN_HUMIDITY   + " REAL NOT NULL, "                    +
                        WeatherContract.WeatherEntry.COLUMN_PRESSURE   + " REAL NOT NULL, "                    +

                        WeatherContract.WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, "                    +
                        WeatherContract.WeatherEntry.COLUMN_DEGREES    + " REAL NOT NULL, "                    +

//              (1) Add a UNIQUE constraint on the date column to replace on conflict
                        /*
                         * To ensure this table can only contain one weather entry per date, we declare
                         * the date column to be unique. We also specify "ON CONFLICT REPLACE". This tells
                         * SQLite that if we have a weather entry for a certain date and we attempt to
                         * insert another weather entry with that date, we replace the old weather entry.
                         */
                        " UNIQUE (" + WeatherContract.WeatherEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        // (1) Add a UNIQUE constraint on the date column to replace on conflict
        /*
         * To ensure this table can only contain one weather entry per date, we declare
         * the date column to be unique. We also specify "ON CONFLICT REPLACE". This tells
         * SQLite that if we have a weather entry for a certain date and we attempt to
         * insert another weather entry with that date, we replace the old weather entry.
         */



        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // (3) Within onUpgrade, drop the weather table if it exists
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherContract.WeatherEntry.TABLE_NAME);
        // (4) call onCreate and pass in the SQLiteDatabase (passed in to onUpgrade)
        onCreate(sqLiteDatabase);

    }

//  (12) Create a public static final String called DATABASE_NAME with value "weather.db"

//  (13) Create a private static final int called DATABASE_VERSION and set it to 1

//  (14) Create a constructor that accepts a context and call through to the superclass constructor

//  (15) Override onCreate and create the weather table from within it

//  (16) Override onUpgrade, but don't do anything within it yet
}
