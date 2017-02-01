package com.example.sean.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sean.popularmovies.data.MovieContract.FavoritesEntry;
import com.example.sean.popularmovies.data.MovieContract.MovieEntry;
import com.example.sean.popularmovies.data.MovieContract.ReviewEntry;
import com.example.sean.popularmovies.data.MovieContract.TrailerEntry;
/**
 * Manages a local database for movie data.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;
    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold movies data.
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE IF NOT EXISTS " +
              MovieEntry.TABLE_NAME + " (" +
              MovieEntry._ID + " INTEGER PRIMARY KEY," +
              MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL," +
              MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
              MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
              MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
              MovieEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
              MovieEntry.COLUMN_AVG_VOTES + " REAL NOT NULL, " +
              MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
              MovieEntry.COLUMN_ISPOPOULAR + " TINYINT NOT NULL, " +
              MovieEntry.COLUMN_ISTOPRATED + " TINYINT NOT NULL, " +

              " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID +  ") ON CONFLICT REPLACE);";

        // Create a table to hold Reviews data.
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE IF NOT EXISTS " +
              ReviewEntry.TABLE_NAME + " (" +
              ReviewEntry._ID + " INTEGER PRIMARY KEY," +
              ReviewEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL, " +
              ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
              ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
              ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
              ReviewEntry.COLUMN_URL + " TEXT NOT NULL, " +

              // Set up the movie_id column as a foreign key to movie table.
              " FOREIGN KEY (" + ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
              MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + "), " +

              " UNIQUE (" + ReviewEntry.COLUMN_REVIEW_ID +  ") ON CONFLICT REPLACE);";

        // Create a table to hold trailer data.
        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE IF NOT EXISTS " +
              TrailerEntry.TABLE_NAME + " (" +
              TrailerEntry._ID + " INTEGER PRIMARY KEY, " +
              TrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
              TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
              TrailerEntry.COLUMN_KEY + " TEXT NOT NULL, " +

              // Set up the movie_id column as a foreign key to movie table.
              " FOREIGN KEY (" + TrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
              MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + "), " +

              " UNIQUE (" + TrailerEntry.COLUMN_KEY +  ") ON CONFLICT REPLACE);";

        // Create a table to hold favorites data.
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE IF NOT EXISTS " +
              FavoritesEntry.TABLE_NAME + " (" +
              FavoritesEntry._ID + " INTEGER PRIMARY KEY, " +
              FavoritesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +

              // Set up the movie_id column as a foreign key to movie table.
              " FOREIGN KEY (" + FavoritesEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
              MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + "), " +

              " UNIQUE (" + TrailerEntry.COLUMN_MOVIE_ID +  ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}