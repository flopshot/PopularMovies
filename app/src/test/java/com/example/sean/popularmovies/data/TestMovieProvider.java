package com.example.sean.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sean.popularmovies.BuildConfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TestMovieProvider {
    private final Context mContext = RuntimeEnvironment.application;
        // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Before
    public void init() throws Exception {
        deleteAllRecords();
    }

    /*
    This test checks to make sure that the content provider is registered correctly.
     */
    @Test
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // MovieProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
              MovieProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            Assert.assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                        " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                  providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                  false);
        }
    }

    /*
        This test doesn't touch the database.  It verifies that the ContentProvider returns
        the correct type for each type of URI that it can handle.
        Students: Uncomment this test to verify that your implementation of GetType is
        functioning correctly.
     */
    @Test
    public void testGetType() {
        String type;

        // content://com.example.sean.popularmovies/movie/
        type = mContext.getContentResolver().getType(MovieContract.MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.sean.popularmovies/weather
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
              MovieContract.MovieEntry.CONTENT_TYPE, type);

        // content://com.example.sean.popularmovies/trailer/
        type = mContext.getContentResolver().getType(MovieContract.TrailerEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.sean.popularmovies/trailer
        assertEquals("Error: the TrailerEntry CONTENT_URI should return TrailerEntry.CONTENT_TYPE",
              MovieContract.TrailerEntry.CONTENT_TYPE, type);

        // content://com.example.sean.popularmovies/review/
        type = mContext.getContentResolver().getType(MovieContract.ReviewEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.sean.popularmovies/review
        assertEquals("Error: the ReviewEntry CONTENT_URI should return ReviewEntry.CONTENT_TYPE",
              MovieContract.ReviewEntry.CONTENT_TYPE, type);

        // content://com.example.sean.popularmovies/favorites
        type = mContext.getContentResolver().getType(MovieContract.FavoritesEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.sean.popularmovies/favorites
        assertEquals("Error: the ReviewEntry CONTENT_URI should return FavoritesEntry.CONTENT_TYPE",
              MovieContract.FavoritesEntry.CONTENT_TYPE, type);
    }

    /*
     This test uses the database directly to insert and then uses the ContentProvider to
     read out the data.  Uncomment this test to see if the basic movie query functionality
     given in the ContentProvider is working correctly.
  */
    @Test
    public void testBasicMovieQuery() {
        // insert our test records into the database
        ContentValues movieValues = getValuesToInsert(0, true);
        ContentValues reviewValues = getValuesToInsert(1, true);
        ContentValues trailerValues = getValuesToInsert(2, true);
        ContentValues favoritesValues = getValuesToInsert(3, true);

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
              MovieContract.MovieEntry.CONTENT_URI,
              null,
              null,
              null,
              null
        );

        Cursor trailerCursor = mContext.getContentResolver().query(
              MovieContract.TrailerEntry.CONTENT_URI,
              null,
              null,
              null,
              null
        );

        Cursor reviewCursor = mContext.getContentResolver().query(
              MovieContract.ReviewEntry.CONTENT_URI,
              null,
              null,
              null,
              null
        );

        Cursor favoritesCursor = mContext.getContentResolver().query(
              MovieContract.FavoritesEntry.CONTENT_URI,
              null,
              null,
              null,
              null
        );

        // Make sure we get the correct cursor out of the database
        validateCursor("testBasicMovieQuery", movieCursor, movieValues);
        validateCursor("testBasicTrailerQuery", trailerCursor, trailerValues);
        validateCursor("testBasicReviewQuery", reviewCursor, reviewValues);
        validateCursor("testBasicFavoritesQuery", favoritesCursor, favoritesValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        //        if ( Build.VERSION.SDK_INT >= 19 ) {
        //            assertEquals("Error: Location Query did not properly set NotificationUri",
        //                  locationCursor.getNotificationUri(), LocationEntry.CONTENT_URI);
        //        }

        movieCursor.close();
        reviewCursor.close();
        trailerCursor.close();
        favoritesCursor.close();
    }

    /*
        This test uses the provider to insert and then update the data. Uncomment this test to
        see if your update location is functioning correctly.
     */
    @Test
    public void testUpdateTable() {
        //TODO
    }

    @Test
    public void testInsertTable() {
        //TODO
    }

    @Test
    public void testBulkInsertTable() {
        //TODO
    }

    @Test
    public void testDeleteTable() {
        //TODO
    }

    private void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
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

    private ContentValues getValuesToInsert(int table, boolean insert) {
        // insert our test records into all database tables
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values;

        switch (table)
        {
            case 0:
                values = new ContentValues();
                values.put(MovieContract.MovieEntry.COLUMN_TITLE, "The Movie");
                values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/z4x0Bp48ar3Mda8KiPD1vwSY3D8.jpg");
                values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2013-01-01");
                values.put(MovieContract.MovieEntry.COLUMN_PLOT, "The movie has a plot");
                values.put(MovieContract.MovieEntry.COLUMN_AVG_VOTES, 7.58);
                values.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 122.6516513);
                values.put(MovieContract.MovieEntry.COLUMN_ISTOPRATED, 1);
                values.put(MovieContract.MovieEntry.COLUMN_ISPOPOULAR, 1);
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 321);
                if (insert) {
                    db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                }
                db.close();
                return values;
            case 1:
                values = new ContentValues();
                values.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, "Seymour Buttes");
                values.put(MovieContract.ReviewEntry.COLUMN_CONTENT, "I LIKED IT!");
                values.put(MovieContract.ReviewEntry.COLUMN_URL, "www.rev.com");
                values.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, "g34g3g34hgqah");
                values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, 321);
                if (insert) {
                    db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                }
                db.close();
                return values;
            case 2:
                values = new ContentValues();
                values.put(MovieContract.TrailerEntry.COLUMN_KEY, "atq3arg");
                values.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, 321);
                values.put(MovieContract.TrailerEntry.COLUMN_NAME, "trailer1");
                if (insert) {
                    db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                }
                db.close();
                return values;
            case 3:
                values = new ContentValues();
                values.put(MovieContract.FavoritesEntry.COLUMN_MOVIE_ID, 321);
                if (insert) {
                    db.insert(MovieContract.FavoritesEntry.TABLE_NAME, null, values);
                }
                db.close();
                return values;
            default:
                db.close();
                return null;
        }
    }

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
              MovieContract.MovieEntry.CONTENT_URI,
              null,
              null
        );
        mContext.getContentResolver().delete(
              MovieContract.TrailerEntry.CONTENT_URI,
              null,
              null
        );
        mContext.getContentResolver().delete(
              MovieContract.ReviewEntry.CONTENT_URI,
              null,
              null
        );
        mContext.getContentResolver().delete(
              MovieContract.FavoritesEntry.CONTENT_URI,
              null,
              null
        );

        Cursor cursor = mContext.getContentResolver().query(
              MovieContract.MovieEntry.CONTENT_URI,
              null,
              null,
              null,
              null
        );
        assertEquals("Error: Records not deleted from Movie table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
              MovieContract.TrailerEntry.CONTENT_URI,
              null,
              null,
              null,
              null
        );
        assertEquals("Error: Records not deleted from Trailer table during delete", 0, cursor.getCount());

        cursor = mContext.getContentResolver().query(
              MovieContract.ReviewEntry.CONTENT_URI,
              null,
              null,
              null,
              null
        );
        assertEquals("Error: Records not deleted from Review table during delete", 0, cursor.getCount());

        cursor = mContext.getContentResolver().query(
              MovieContract.FavoritesEntry.CONTENT_URI,
              null,
              null,
              null,
              null
        );
        assertEquals("Error: Records not deleted from Favorites table during delete", 0, cursor.getCount());
        cursor.close();
    }

    /*
        Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }
}
