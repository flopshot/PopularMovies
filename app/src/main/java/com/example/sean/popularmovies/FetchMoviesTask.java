package com.example.sean.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.sean.popularmovies.data.api.RequestMovieData;
import com.example.sean.popularmovies.data.api.StoreMovieData;

/**
 * Refactored AsyncTask
 */
class FetchMoviesTask extends AsyncTask<Void, Void, Void> {
    //The Async Task generates a background thread which downloads the movie data from
    //The Movie DB api. The movie data comes in the form of a JSON string with movie titles
    //of Popular or Top Rated movies based on user preference
    private Context mContext;
    private MovieThumbnailAdapter mMovieAdapter; //TODO: Get rid of this when using cursoradapter

    FetchMoviesTask(Context c, MovieThumbnailAdapter mta) {
        this.mContext = c;
        this.mMovieAdapter = mta;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        //We pass the Movie DB query to the background thread
        RequestMovieData rMovies = new RequestMovieData(mContext);
        String popularMovies = rMovies.getMovieData(0, null);
        String topMovies = rMovies.getMovieData(1, null);
        Intent noNetworkIntent;

        if(popularMovies.equals("No Network Connection") || topMovies.equals("No Network Connection")) {
            noNetworkIntent = new Intent();
            noNetworkIntent.setClassName(mContext
                  , NoNetworkActivity.class.getName()
            );
            mContext.startActivity(noNetworkIntent);
        } else {
            StoreMovieData movieData = new StoreMovieData(mContext);
            String[] movieIds = movieData.insertIntoMovies(popularMovies, topMovies);

            for (String id: movieIds) {
                String reviewMovieData = rMovies.getMovieData(3, id);
                movieData.insertIntoReviews(reviewMovieData);

                String trailerMovieData = rMovies.getMovieData(2, id);
                movieData.insertIntoTrailers(trailerMovieData);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //
    }
}
