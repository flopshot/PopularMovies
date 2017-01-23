package com.example.sean.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used as a helper/utility class in the MovieArrayCreator class to parse JSON string
 * of specific movie data values from API.
 */
class MovieDataParser{
    String getMoviePlot(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray("results");
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString("overview");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    String getMovieReleaseDate(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray("results");
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString("release_date");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    String getMovieAvgVotes(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray("results");
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString("vote_average");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    String getMovieTitle(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray("results");
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString("title");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    String getMoviePosterPath(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray("results");
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString("poster_path");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    int getMovieCount(String movieJsonStr) {
        try{
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray("results");
            return moviesJson.length();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
