package com.example.sean.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * This Fragment contains the details of the movie clicked in the listView
 */
public class DetailFragment extends Fragment {
    TextView titleView, plotView, releaseDateView, ratingView;
    ImageView posterView;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        MovieThumbnail movie = this.getArguments().getParcelable("movieDetail");

        //Write movie details to views in fragment
        titleView = (TextView) rootView.findViewById(R.id.detailTitle);
        plotView = (TextView) rootView.findViewById(R.id.detailPlotValue);
        ratingView = (TextView) rootView.findViewById(R.id.detailRatingValue);
        releaseDateView = (TextView) rootView.findViewById(R.id.detailReleaseDateValue);
        posterView = (ImageView) rootView.findViewById(R.id.detailPoster);

        titleView.setText(movie.movieTitle);
        plotView.setText(movie.moviePlot);
        releaseDateView.setText(movie.movieReleaseDate);
        ratingView.setText(movie.movieAvgVotes);
        Picasso.with(getContext())
                .load(movie.movieImage)
                .placeholder(R.drawable.movie_icon)
                .error(R.drawable.movie_icon)
                .into(posterView);

        return rootView;
    }
}
