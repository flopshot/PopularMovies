package com.example.sean.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import com.example.sean.popularmovies.data.MovieContract;

/**
 * This adapter syncs with the trailer and reviews table and for the expandable listview in the
 * details fragment
 */

 class MediaListAdapter extends SimpleCursorTreeAdapter {
    private Context mContext;
    private String mMovieId;
    private SQLiteDatabase mDb;
    private String trailerHeader, reviewHeader, mHeaderCol;

    MediaListAdapter(String headerCol, SQLiteDatabase db, String movieId, Context context,
                     Cursor cursor, int groupLayout, String[] groupFrom, int[] groupTo,
                     int childLayout, String[] childFrom, int[] childTo) {
        super(context, cursor, groupLayout, groupFrom, groupTo, childLayout, childFrom, childTo);
        mMovieId = movieId;
        mContext = context;
        mDb = db;
        mHeaderCol = headerCol;

        //Get resource strings for Group headers in expandable list
        trailerHeader = mContext.getResources().getString(R.string.trailerLabel);
        reviewHeader = mContext.getResources().getString(R.string.reviewLabel);
    }

    @Override
    public View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        return super.newChildView(context, cursor, isLastChild, parent);
    }

    @Override
    public Cursor getChild(int groupPosition, int childPosition) {
        Cursor c = getChildrenCursor(getCursor());
        if (c != null) {
            return c;
        } else {return null;}
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        View childView;
        Cursor c = getChildrenCursor(getCursor());
        c.moveToPosition(childPosition);
        switch (groupPosition) {
            case 0: //Trailer Child View
                childView = LayoutInflater
                      .from(mContext).inflate(R.layout.trailer_item, parent, false);
                TextView titleText = (TextView) childView.findViewById(R.id.trailer_item_title);
                String trailerText = c.getString(
                                        c.getColumnIndex(MovieContract.TrailerEntry.COLUMN_NAME));
                titleText.setText(trailerText);
                return childView;
            case 1: //Review Child View
                childView = LayoutInflater
                      .from(mContext).inflate(R.layout.review_item, parent, false);
                TextView authorText = (TextView) childView.findViewById(R.id.review_item_author);
                String authorTitle = c.getString(
                                        c.getColumnIndex(MovieContract.ReviewEntry.COLUMN_AUTHOR));
                authorText.setText(authorTitle);
                TextView contentText = (TextView) childView.findViewById(R.id.review_item_content);
                String contentTitle = c.getString(
                                        c.getColumnIndex(MovieContract.ReviewEntry.COLUMN_CONTENT));
                contentText.setText(contentTitle);
                return childView;
            default:
                return null;
        }
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        String cursorType = groupCursor.getString(groupCursor.getColumnIndex(mHeaderCol));
        Cursor childCursor;
        if (cursorType.equals(trailerHeader)) {
            String query = "SELECT _id, " + MovieContract.TrailerEntry.COLUMN_NAME
                  + ", " + MovieContract.TrailerEntry.COLUMN_KEY + " FROM "
                  + MovieContract.TrailerEntry.TABLE_NAME + " WHERE "
                  + MovieContract.TrailerEntry.COLUMN_MOVIE_ID + "=" + mMovieId + ";";
            childCursor = mDb.rawQuery(query, null);
            return childCursor;
        } else if (cursorType.equals(reviewHeader)) {
            String query = "SELECT _id, "
                  + MovieContract.ReviewEntry.COLUMN_AUTHOR
                  + ", " + MovieContract.ReviewEntry.COLUMN_CONTENT
                  + ", " + MovieContract.ReviewEntry.COLUMN_URL
                  + " FROM " + MovieContract.ReviewEntry.TABLE_NAME + " WHERE "
                  + MovieContract.ReviewEntry.COLUMN_MOVIE_ID + "=" + mMovieId + ";";
            childCursor = mDb.rawQuery(query, null);
            return childCursor;
        } else {
            return null;
        }
    }
}