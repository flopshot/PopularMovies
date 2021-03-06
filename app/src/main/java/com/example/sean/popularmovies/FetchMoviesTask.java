package com.example.sean.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.widget.Toast;

import com.example.sean.popularmovies.data.api.RequestMovieData;
import com.example.sean.popularmovies.data.api.StoreMovieData;

/**
 * Refactored AsyncTask
 */
class FetchMoviesTask extends AsyncTask<Void, String, Void> {
    //The Async Task generates a background thread which downloads the movie data from
    //The Movie DB api. The movie data comes in the form of a JSON string with movie titles
    //of Popular or Top Rated movies based on user preference
    private Context mContext;
    private int mLoaderId;
    private LoaderManager mManager;
    private LoaderManager.LoaderCallbacks mLoaderCallback;
    private String popularMovies;
    private String topMovies;
    private MovieFragment mMovieFragment;

    FetchMoviesTask(Context c, LoaderManager manager, int loaderId,
                    LoaderManager.LoaderCallbacks loaderCallback, MovieFragment movieFragment) {
        this.mContext = c;
        this.mManager = manager;
        this.mLoaderId = loaderId;
        this.mLoaderCallback = loaderCallback;
        this.mMovieFragment = movieFragment;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        //We pass the Movie DB query to the background thread
        RequestMovieData rMovies = new RequestMovieData(mContext);
        popularMovies = rMovies.getMovieData(0, null);
        topMovies = rMovies.getMovieData(1, null);
        Intent noNetworkIntent;

        if(popularMovies.equals("No Network Connection") || topMovies.equals("No Network Connection")) {
            noNetworkIntent = new Intent();
            noNetworkIntent.setClassName(mContext
                  , NoNetworkActivity.class.getName()
            );
            noNetworkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(noNetworkIntent);
        } else {
            publishProgress(mContext.getResources().getString(R.string.movie_updates));
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
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(mContext, values[0], Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(!popularMovies.equals("No Network Connection")
              && !topMovies.equals("No Network Connection")) {
            if (mMovieFragment.isAdded())
                mManager.restartLoader(mLoaderId, null, mLoaderCallback);
                Toast.makeText(mContext,
                      mContext.getString(R.string.movie_updates_finish),
                      Toast.LENGTH_LONG
                ).show();
        }
    }
}
