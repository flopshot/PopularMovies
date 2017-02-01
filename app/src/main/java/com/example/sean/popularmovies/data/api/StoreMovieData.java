package com.example.sean.popularmovies.data.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.sean.popularmovies.data.MovieContract;
import com.example.sean.popularmovies.data.MovieDbHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Used to extract data from JSON string returned from API and stored in database
 */
public class StoreMovieData {
    private Context mContext;
    private MovieDbHelper helper;
    private SQLiteDatabase db;
    private ContentValues values;

    public StoreMovieData(Context c) {
        this.mContext = c;
    }

    public String[] insertIntoMovies(String pString, String tString) {
        helper = new MovieDbHelper(mContext);
        db = helper.getWritableDatabase();
        values = new ContentValues();

        //extract popular movies data from JSON string and insert into database
        int pCnt = ApiDataParser.getItemCount(pString, ApiDataParser.TOP_LEVEL_ARRAY_LABEL);
        String[] pMovieIds = new String[pCnt];
        for (int i = 0; i < pCnt; i++) {
            pMovieIds[i] = ApiDataParser.getId(pString, i);
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, pMovieIds[i]);
            values.put(MovieContract.MovieEntry.COLUMN_TITLE,
                  ApiDataParser.getMovieTitle(pString, i));
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, 
                  ApiDataParser.getReleaseDate(pString, i));
            values.put(MovieContract.MovieEntry.COLUMN_PLOT,
                  ApiDataParser.getMoviePlot(pString, i));
            values.put(MovieContract.MovieEntry.COLUMN_POPULARITY,
                  ApiDataParser.getPopularity(pString, i));
            values.put(MovieContract.MovieEntry.COLUMN_AVG_VOTES,
                  ApiDataParser.getMovieAvgVotes(pString, i));
            values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                  ApiDataParser.getMoviePosterPath(pString,i));
            values.put(MovieContract.MovieEntry.COLUMN_ISPOPOULAR,
                  "1");
            values.put(MovieContract.MovieEntry.COLUMN_ISTOPRATED,
                  "0");
            db.insert(MovieContract.MovieEntry.TABLE_NAME, "", values);
        }

        //extract top_rated movies data from JSON string and insert into database
        int tCnt = ApiDataParser.getItemCount(tString, ApiDataParser.TOP_LEVEL_ARRAY_LABEL);
        String[] tMovieIds = new String[tCnt];
        for (int i = 0; i < tCnt; i++) {
            tMovieIds[i] = ApiDataParser.getId(tString, i);
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, tMovieIds[i]);
            values.put(MovieContract.MovieEntry.COLUMN_TITLE,
                  ApiDataParser.getMovieTitle(tString, i));
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                  ApiDataParser.getReleaseDate(tString, i));
            values.put(MovieContract.MovieEntry.COLUMN_PLOT,
                  ApiDataParser.getMoviePlot(tString, i));
            values.put(MovieContract.MovieEntry.COLUMN_POPULARITY,
                  ApiDataParser.getPopularity(tString, i));
            values.put(MovieContract.MovieEntry.COLUMN_AVG_VOTES,
                  ApiDataParser.getMovieAvgVotes(tString, i));
            values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                  ApiDataParser.getMoviePosterPath(tString,i));
            values.put(MovieContract.MovieEntry.COLUMN_ISPOPOULAR,
                  "0");
            values.put(MovieContract.MovieEntry.COLUMN_ISTOPRATED,
                  "1");
            db.insert(MovieContract.MovieEntry.TABLE_NAME, "", values);
        }
        values.clear();

        //Flag all movies that are both popular and top rated in the db
        Set<String> tMoviesSet = new HashSet<>(Arrays.asList(tMovieIds));
        Set<String> pMoviesSet = new HashSet<>(Arrays.asList(pMovieIds));
        tMoviesSet.retainAll(pMoviesSet);

        if (!tMoviesSet.isEmpty()) {
            String[] movieIdArgs = tMoviesSet.toArray(new String[tMoviesSet.size()]);
            values.put(MovieContract.MovieEntry.COLUMN_ISPOPOULAR, "1");
            values.put(MovieContract.MovieEntry.COLUMN_ISTOPRATED, "1");
            db.update(MovieContract.MovieEntry.TABLE_NAME,
                  values,
                  MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                  movieIdArgs);

            values.clear();
        }

        tMoviesSet = new HashSet<>(Arrays.asList(tMovieIds));
        tMoviesSet.addAll(pMoviesSet);

        helper.close();
        db.close();
        return tMoviesSet.toArray(new String[tMoviesSet.size()]);
    }

    public void insertIntoReviews(String reviewJsonStr) {
        helper = new MovieDbHelper(mContext);
        db = helper.getWritableDatabase();
        values = new ContentValues();
        String movieId = ApiDataParser.getSingleMovieId(reviewJsonStr);

        //extract popular review data from JSON string and insert into database
        int pCnt = ApiDataParser.getItemCount(reviewJsonStr, ApiDataParser.TOP_LEVEL_ARRAY_LABEL);
        for (int i = 0; i < pCnt; i++) {
            values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, movieId);
            values.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID,
                  ApiDataParser.getId(reviewJsonStr, i));
            values.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,
                  ApiDataParser.getAuthor(reviewJsonStr, i));
            values.put(MovieContract.ReviewEntry.COLUMN_CONTENT,
                  ApiDataParser.getReview(reviewJsonStr, i));
            values.put(MovieContract.ReviewEntry.COLUMN_URL,
                  ApiDataParser.getReveiewLink(reviewJsonStr, i));
            db.insert(MovieContract.ReviewEntry.TABLE_NAME, "", values);
        }
        values.clear();
        helper.close();
        db.close();
    }
    
    public void insertIntoTrailers(String trailerJsonStr) {
        helper = new MovieDbHelper(mContext);
        db = helper.getWritableDatabase();
        values = new ContentValues();
        String movieId = ApiDataParser.getSingleMovieId(trailerJsonStr);

        //extract trailer data from JSON string and insert into database
        int pCnt = ApiDataParser.getItemCount(trailerJsonStr, ApiDataParser.TOP_LEVEL_TRAILER_ARRAY_LABEL);
        for (int i = 0; i < pCnt; i++) {
            values.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, movieId);
            values.put(MovieContract.TrailerEntry.COLUMN_NAME,
                  ApiDataParser.getTrailerName(trailerJsonStr, i));
            values.put(MovieContract.TrailerEntry.COLUMN_KEY,
                  ApiDataParser.getYoutubeKey(trailerJsonStr, i));
            db.insert(MovieContract.TrailerEntry.TABLE_NAME, "", values);
        }
        values.clear();
        helper.close();
        db.close();
    }
}