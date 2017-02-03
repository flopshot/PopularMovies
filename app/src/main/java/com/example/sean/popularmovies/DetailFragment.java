package com.example.sean.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sean.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import static com.example.sean.popularmovies.Utility.getFormattedDate;
import static com.example.sean.popularmovies.Utility.round;

/**
 * This Fragment contains the details of the movie clicked in the listView
 */
public class DetailFragment extends Fragment {
    TextView titleView, plotView, releaseDateView, ratingView;
    ImageView posterView;
    CursorLoader loader;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        String movieId = this
              .getArguments()
              .getString(MovieContract.MovieEntry.COLUMN_MOVIE_ID);

        // Get Movie Details From movie table using movie content uri
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        loader = new CursorLoader(getActivity().getApplicationContext(),
                            uri,
                            null,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=?",
                            new String[] {movieId},
                            null);
        loader.registerListener(42, new MyOnLoadCompleteListener());
        loader.startLoading();

        //Write movie details to views in fragment
        titleView = (TextView) rootView.findViewById(R.id.detailTitle);
        plotView = (TextView) rootView.findViewById(R.id.detailPlotValue);
        ratingView = (TextView) rootView.findViewById(R.id.detailRatingValue);
        releaseDateView = (TextView) rootView.findViewById(R.id.detailReleaseDateValue);
        posterView = (ImageView) rootView.findViewById(R.id.detailPoster);

        return rootView;
    }

    class MyOnLoadCompleteListener implements OnLoadCompleteListener<Cursor> {
        @Override
        public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
            if (cursor != null && cursor.moveToFirst()) {
                titleView.setText(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
                plotView.setText(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PLOT)));
                Picasso.with(getContext())
                      .load(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)))
                      .placeholder(R.drawable.movie_icon)
                      .error(R.drawable.movie_icon)
                      .into(posterView);

                String ratingText = String.valueOf(
                      round(cursor.getFloat(cursor.getColumnIndex(
                            MovieContract.MovieEntry.COLUMN_AVG_VOTES)), 1
                      )
                );
                releaseDateView.setText(getFormattedDate(
                      cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE))));
                ratingView.setText(ratingText);
            }
        }
    }
}