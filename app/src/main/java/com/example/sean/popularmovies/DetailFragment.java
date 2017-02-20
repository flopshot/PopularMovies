package com.example.sean.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sean.popularmovies.data.MovieContract;
import com.example.sean.popularmovies.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

import static com.example.sean.popularmovies.Utility.getFormattedDate;
import static com.example.sean.popularmovies.Utility.round;
import static com.example.sean.popularmovies.data.MovieDbHelper.getHelper;

/**
 * This Fragment contains the details of the movie clicked in the listView
 */
public class DetailFragment extends Fragment
      implements LoaderManager.LoaderCallbacks<Cursor>, CompoundButton.OnCheckedChangeListener {
    private TextView titleView, plotView, releaseDateView, ratingView;
    private ToggleButton favoritesButton;
    private ImageView posterView;
    private String movieId, trailerHeader, reviewHeader;
    private MediaListAdapter mediaAdapter;
    private SQLiteDatabase db = null;

    private ContentResolver resolver = null;

    private static final Uri FAVORITES_URI = MovieContract.FavoritesEntry.CONTENT_URI;
    private static final Uri DETAILS_URI = MovieContract.MovieEntry.CONTENT_URI;
    private static final int DETAIL_LOADER = 101;
    private static final int FAVORITES_LOADER = 102;
    private static final String HEADER_LABEL = "header";
    private static final String GROUP_CURSOR_ID = "_ID";

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //get movie_id from activity bundle and initiate database for expandable list
        MovieDbHelper helper = getHelper(getActivity().getApplicationContext());
        db = helper.getReadableDatabase();
        movieId = this.getArguments().getString(MovieContract.MovieEntry.COLUMN_MOVIE_ID);

        resolver = getActivity().getContentResolver();

        //Get resource strings for Group headers in expandable list
        trailerHeader = getResources().getString(R.string.trailerLabel);
        reviewHeader = getResources().getString(R.string.reviewLabel);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(FAVORITES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        String[] groupFrom = new String[] {HEADER_LABEL, GROUP_CURSOR_ID};
        String[] childFrom = new String[] {HEADER_LABEL};
        int[] childTo = new int[] {R.id.trailer_item_title};
        int[] groupTo = new int[] {R.id.group_title};
        MatrixCursor matrixCursor = new MatrixCursor(groupFrom);
        matrixCursor.addRow(new String[] {trailerHeader, "0"});
        matrixCursor.addRow(new String[] {reviewHeader, "1"});

        mediaAdapter = new MediaListAdapter(HEADER_LABEL,
              db,
              movieId,
              getActivity().getApplicationContext(),
              matrixCursor,
              R.layout.expandable_group_header,
              groupFrom,
              groupTo,
              R.layout.trailer_item,
              childFrom,
              childTo
        );

        ExpandableListView mediaView = (ExpandableListView) rootView.findViewById(R.id.trailerGroup);
        mediaView.setAdapter(mediaAdapter);
        mediaView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String url;
                Intent mediaIntent = new Intent();
                mediaIntent.setAction(Intent.ACTION_VIEW);
                Cursor c = mediaAdapter.getChild(groupPosition, childPosition);
                if (c.moveToFirst()) {
                    c.moveToPosition(childPosition);
                    if (groupPosition == 0) {
                        url = c.getString(c.getColumnIndex(MovieContract.TrailerEntry.COLUMN_KEY));
                        mediaIntent.setData(Uri.parse(url));
                        startActivity(mediaIntent);
                    } else if (groupPosition ==1) {
                        url = c.getString(c.getColumnIndex(MovieContract.ReviewEntry.COLUMN_URL));
                        mediaIntent.setData(Uri.parse(url));
                        startActivity(mediaIntent);
                    }
                }
                return false;
            }
        });

        //Write movie details to views in fragment
        titleView = (TextView) rootView.findViewById(R.id.detailTitle);
        plotView = (TextView) rootView.findViewById(R.id.detailPlotValue);
        ratingView = (TextView) rootView.findViewById(R.id.detailRatingValue);
        releaseDateView = (TextView) rootView.findViewById(R.id.detailReleaseDateValue);
        posterView = (ImageView) rootView.findViewById(R.id.detailPoster);

        favoritesButton = (ToggleButton) rootView.findViewById(R.id.favorite);
        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ContentValues values = new ContentValues();
        if (isChecked) {
            values.put(MovieContract.FavoritesEntry.COLUMN_MOVIE_ID, movieId);
            resolver.insert(MovieContract.FavoritesEntry.CONTENT_URI, values);
            values.clear();
        } else {
            resolver.delete(MovieContract.FavoritesEntry.CONTENT_URI,
                  MovieContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?",
                  new String[] {movieId});

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch(loaderId) {
            case DETAIL_LOADER:
                // Get Movie Details From movie table using movie content uri for detail views
                return new CursorLoader(getActivity().getApplicationContext(),
                      DETAILS_URI,
                      null,
                      MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                      new String[]{movieId},
                      null);
            case FAVORITES_LOADER:
                // Check if movie is on the user favorites list
                return new CursorLoader(getActivity().getApplication(),
                      FAVORITES_URI,
                      null,
                      MovieContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?",
                      new String[] {movieId},
                      null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch(loader.getId()) {
            case DETAIL_LOADER:
                if (cursor != null && cursor.moveToFirst()) {

                    titleView.setText(
                          cursor.getString(
                                cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
                    plotView.setText(
                          cursor.getString(
                                cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PLOT)));
                    Picasso.with(getContext())
                          .load(
                                cursor.getString(
                                      cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)))
                          .placeholder(R.drawable.movie_icon)
                          .error(R.drawable.movie_icon)
                          .into(posterView);

                    String ratingText = String.valueOf(
                          round(
                                cursor.getFloat(
                                      cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_AVG_VOTES)), 1));
                    releaseDateView.setText(
                          getFormattedDate(
                                cursor.getLong(
                                    cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE))));
                    ratingView.setText(ratingText);
                }
                break;
            case FAVORITES_LOADER:
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        favoritesButton.setChecked(true);
                    } else  {
                        favoritesButton.setChecked(false);
                    }
                }
                // Setting ContentObserver listener is not triggered when toggle initially sets
                favoritesButton.setOnCheckedChangeListener(this);
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()) {
            case DETAIL_LOADER:
                break;
            case FAVORITES_LOADER:
                break;
            default:
                break;
        }
    }
}