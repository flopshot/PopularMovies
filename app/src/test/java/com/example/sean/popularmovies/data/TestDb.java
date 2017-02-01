package com.example.sean.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sean.popularmovies.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Todo: Test entry uniqueness on all tables
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TestDb {

    private final Context mContext = RuntimeEnvironment.application;

    private void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    @Before
    public void init() {
        deleteTheDatabase();
    }

    @Test
    public void testCreateDb() throws Exception{
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.ReviewEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.TrailerEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.FavoritesEntry.TABLE_NAME);

        SQLiteDatabase db = new MovieDbHelper(
              this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
              c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain all the tables
        assertTrue("Error: Your database was created without all entry tables",
              tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns in Movie table?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")",
              null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
              c.moveToFirst());

        // Build a HashSet of all of the column names in the movie table we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<>();
        movieColumnHashSet.add(MovieContract.MovieEntry._ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_TITLE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_TITLE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_AVG_VOTES);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POPULARITY);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_PLOT);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_ISPOPOULAR);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_ISTOPRATED);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required movie
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns",
              movieColumnHashSet.isEmpty());
        
        // now, do our tables contain the correct columns in review table?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.ReviewEntry.TABLE_NAME + ")",
              null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
              c.moveToFirst());

        // Build a HashSet of all of the column names in the review table we want to look for
        final HashSet<String> reviewColumnHashSet = new HashSet<>();
        reviewColumnHashSet.add(MovieContract.ReviewEntry._ID);
        reviewColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_MOVIE_ID);
        reviewColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_URL);
        reviewColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_AUTHOR);
        reviewColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_CONTENT);

        columnNameIndex = c.getColumnIndex("name");

        do {
            String columnName = c.getString(columnNameIndex);
            reviewColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required review
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required review entry columns",
              reviewColumnHashSet.isEmpty());

        // now, do our tables contain the correct columns in trailer table?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.TrailerEntry.TABLE_NAME + ")",
              null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
              c.moveToFirst());

        // Build a HashSet of all of the column names in the favorites table we want to look for
        final HashSet<String> trailerColumnHashSet = new HashSet<>();
        trailerColumnHashSet.add(MovieContract.TrailerEntry._ID);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COLUMN_MOVIE_ID);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COLUMN_NAME);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COLUMN_KEY);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            trailerColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required trailer
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required trailer entry columns",
              trailerColumnHashSet.isEmpty());

        // now, do our tables contain the correct columns in favorites table?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.FavoritesEntry.TABLE_NAME + ")",
              null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
              c.moveToFirst());

        // Build a HashSet of all of the column names in the favorites table we want to look for
        final HashSet<String> favoritesColumnHashSet = new HashSet<>();
        favoritesColumnHashSet.add(MovieContract.TrailerEntry._ID);
        favoritesColumnHashSet.add(MovieContract.TrailerEntry.COLUMN_MOVIE_ID);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            favoritesColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required favorits
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required favorites entry columns",
              favoritesColumnHashSet.isEmpty());
        c.close();
        db.close();
    }

    @Test
    public void testMovieTable() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "The Movie");
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/z4x0Bp48ar3Mda8KiPD1vwSY3D8.jpg");
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2013-01-01");
        movieValues.put(MovieContract.MovieEntry.COLUMN_PLOT, "The movie has a plot");
        movieValues.put(MovieContract.MovieEntry.COLUMN_AVG_VOTES, 7.58);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 122.3215615);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ISTOPRATED, 1);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ISPOPOULAR, 1);
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 321);

        // Third Step: Insert ContentValues into database and get a row ID back
        long locationRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, movieValues);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
              MovieContract.MovieEntry.TABLE_NAME,  // Table to Query
              null, // all columns
              null, // Columns for the "where" clause
              null, // Values for the "where" clause
              null, // columns to group by
              null, // columns to filter by row groups
              null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from movie query", cursor.moveToFirst() );

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        validateCurrentRecord("Error: Movie Query Validation Failed",
              cursor, movieValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from Movie query",
              cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
    }

    @Test
    public void testReviewTable() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of movie entry and insert into db
        // with corresponding movie_id to maintain foreign key integrity
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "The Movie");
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/z4x0Bp48ar3Mda8KiPD1vwSY3D8.jpg");
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2013-01-01");
        movieValues.put(MovieContract.MovieEntry.COLUMN_PLOT, "The movie has a plot");
        movieValues.put(MovieContract.MovieEntry.COLUMN_AVG_VOTES, 7.58);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 122.3215615);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ISTOPRATED, 1);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ISPOPOULAR, 1);
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 321);
        db.insert(MovieContract.MovieEntry.TABLE_NAME, null, movieValues);

        // Second Step: Create ContentValues of what you want to insert
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, "Seymour Buttes");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, "awaw3gaerga4w");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, "I LIKED IT!");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_URL, "www.rev.com");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, 321);

        // Third Step: Insert ContentValues into database and get a row ID back
        long locationRowId = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, reviewValues);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
              MovieContract.ReviewEntry.TABLE_NAME,  // Table to Query
              null, // all columns
              null, // Columns for the "where" clause
              null, // Values for the "where" clause
              null, // columns to group by
              null, // columns to filter by row groups
              null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from review query", cursor.moveToFirst() );

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        validateCurrentRecord("Error: Review Query Validation Failed",
              cursor, reviewValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from review query",
              cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
    }

    @Test
    public void testTrailerTable() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of movie entry and insert into db
        // with corresponding movie_id to maintain foreign key integrity
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "The Movie");
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/z4x0Bp48ar3Mda8KiPD1vwSY3D8.jpg");
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2013-01-01");
        movieValues.put(MovieContract.MovieEntry.COLUMN_PLOT, "The movie has a plot");
        movieValues.put(MovieContract.MovieEntry.COLUMN_AVG_VOTES, 7.58);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 122.3215615);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ISTOPRATED, 1);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ISPOPOULAR, 1);
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 322);
        db.insert(MovieContract.MovieEntry.TABLE_NAME, null, movieValues);


        // Second Step: Create ContentValues of what you want to insert
        ContentValues trailerValues = new ContentValues();
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_KEY, "atq3arg");
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, 322);
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_NAME, "trailer1");

        // Third Step: Insert ContentValues into database and get a row ID back
        long locationRowId = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, trailerValues);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
              MovieContract.TrailerEntry.TABLE_NAME,  // Table to Query
              null, // all columns
              null, // Columns for the "where" clause
              null, // Values for the "where" clause
              null, // columns to group by
              null, // columns to filter by row groups
              null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from TRAILER query", cursor.moveToFirst() );

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        validateCurrentRecord("Error: Trailer Query Validation Failed",
              cursor, trailerValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from trailer query",
              cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
    }

    @Test
    public void testFavoritesTable() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of movie entry and insert into db
        // with corresponding movie_id to maintain foreign key integrity
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "The Movie");
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/z4x0Bp48ar3Mda8KiPD1vwSY3D8.jpg");
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2013-01-01");
        movieValues.put(MovieContract.MovieEntry.COLUMN_PLOT, "The movie has a plot");
        movieValues.put(MovieContract.MovieEntry.COLUMN_AVG_VOTES, 7.58);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 122.3215615);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ISTOPRATED, 1);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ISPOPOULAR, 1);
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 322);
        db.insert(MovieContract.MovieEntry.TABLE_NAME, null, movieValues);


        // Second Step: Create ContentValues of what you want to insert
        ContentValues favoritesValues = new ContentValues();
        favoritesValues.put(MovieContract.FavoritesEntry.COLUMN_MOVIE_ID, 322);

        // Third Step: Insert ContentValues into database and get a row ID back
        long locationRowId = db.insert(MovieContract.FavoritesEntry.TABLE_NAME, null, favoritesValues);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
              MovieContract.FavoritesEntry.TABLE_NAME,  // Table to Query
              null, // all columns
              null, // Columns for the "where" clause
              null, // Values for the "where" clause
              null, // columns to group by
              null, // columns to filter by row groups
              null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from FAVORITES query", cursor.moveToFirst() );

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        validateCurrentRecord("Error: Favorites Query Validation Failed",
              cursor, favoritesValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from favorites query",
              cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
    }

    private void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                  "' did not match the expected value '" +
                  expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}