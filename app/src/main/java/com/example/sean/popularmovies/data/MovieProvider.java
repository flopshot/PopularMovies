package com.example.sean.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Content Provider manages movie info, trailer, and review data requests from applications
 */
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SQLiteDatabase db;
    static final int MOVIE = 100;
    static final int TRAILER = 101;
    static final int REVIEW = 102;
    static final int FAVORITES = 103;

    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILER);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_FAVORITES, FAVORITES);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        MovieDbHelper mOpenHelper = new MovieDbHelper(getContext());
        db = mOpenHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] columns, String whereClause, String[] whereArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return db.query(
                        MovieContract.MovieEntry.TABLE_NAME, columns,
                        whereClause, whereArgs, null, null, sortOrder
                );
            case TRAILER:
                return db.query(
                        MovieContract.TrailerEntry.TABLE_NAME, columns,
                        whereClause, whereArgs, null, null, sortOrder
                );
            case REVIEW:
                return db.query(
                        MovieContract.ReviewEntry.TABLE_NAME, columns,
                        whereClause, whereArgs, null, null, sortOrder
                );
            case FAVORITES:
                return db.query(
                      MovieContract.FavoritesEntry.TABLE_NAME, columns,
                      whereClause, whereArgs, null, null, sortOrder
                );
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public String getType(@NonNull Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case FAVORITES:
                return MovieContract.FavoritesEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW: {
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0)
                    returnUri = MovieContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            }
            case TRAILER: {
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0)
                    returnUri = MovieContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            }
            case FAVORITES: {
                long _id = db.insert(MovieContract.FavoritesEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0)
                    returnUri = MovieContract.FavoritesEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri,@NonNull ContentValues[] values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                bulkInsertHelper(values, uri, MovieContract.MovieEntry.TABLE_NAME);
            case TRAILER:
                bulkInsertHelper(values, uri, MovieContract.TrailerEntry.TABLE_NAME);
            case REVIEW:
                bulkInsertHelper(values, uri, MovieContract.ReviewEntry.TABLE_NAME);
            case FAVORITES:
                bulkInsertHelper(values, uri, MovieContract.FavoritesEntry.TABLE_NAME);
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String whereClause, String[] whereArgs) {
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == whereClause ) whereClause = "1";
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(
                      MovieContract.MovieEntry.TABLE_NAME, whereClause, whereArgs);
                break;
            case REVIEW:
                rowsDeleted = db.delete(
                      MovieContract.ReviewEntry.TABLE_NAME, whereClause, whereArgs);
                break;
            case TRAILER:
                rowsDeleted = db.delete(
                      MovieContract.TrailerEntry.TABLE_NAME, whereClause, whereArgs);
                break;
            case FAVORITES:
                rowsDeleted = db.delete(
                      MovieContract.FavoritesEntry.TABLE_NAME, whereClause, whereArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String whereClause
          ,String[] whereArgs) {
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,
                      values, whereClause, whereArgs);
                break;
            case TRAILER:
                rowsUpdated = db.update(MovieContract.TrailerEntry.TABLE_NAME,
                      values, whereClause, whereArgs);
                break;
            case REVIEW:
                rowsUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME,
                      values, whereClause, whereArgs);
                break;
            case FAVORITES:
                rowsUpdated = db.update(MovieContract.FavoritesEntry.TABLE_NAME,
                      values, whereClause, whereArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    /**
     * Helper method to reduce code duplication in bulkInsert method
     */
    private int bulkInsertHelper(ContentValues[] values, Uri uri, String table) {
        int returnCount = 0;
        db.beginTransaction();
        try {
            for (ContentValues value:values) {
                long _id = db.insert(table, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }
}