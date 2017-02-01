package com.example.sean.popularmovies.data.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * JSON data extraction methods for return JSON string from Movie DB API
 */

class ApiDataParser {
    static final String TOP_LEVEL_ARRAY_LABEL = "results";
    static final String TOP_LEVEL_TRAILER_ARRAY_LABEL = "youtube";
    private static final String PLOT_LABEL = "overview";
    private static final String TRAILER_NAME_LABEL = "name";
    private static final String RELEASE_DATE_LABEL = "release_date";
    private static final String VOTE_AVG_LABEL = "vote_average";
    private static final String TITLE_LABEL = "title";
    private static final String POSTER_PATH_LABEL = "poster_path";
    private static final String ID_LABEL = "id";
    private static final String API_STRING_DATE_FORMAT = "yyyy-MM-dd";
    private static final String AUTHOR_LABEL = "author";
    private static final String REVIEW_LABEL = "content";
    private static final String REVIEW_LINK_LABEL = "url";
    private static final String YOUTUBE_KEY_LABEL = "source";
    private static final String POPULARITY_LABEL = "popularity";

    static String getPopularity(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray(TOP_LEVEL_ARRAY_LABEL);
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString(POPULARITY_LABEL);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getYoutubeKey(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray(TOP_LEVEL_TRAILER_ARRAY_LABEL);
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString(YOUTUBE_KEY_LABEL);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getReveiewLink(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray(TOP_LEVEL_ARRAY_LABEL);
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString(REVIEW_LINK_LABEL);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getReview(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray(TOP_LEVEL_ARRAY_LABEL);
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString(REVIEW_LABEL);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getReleaseDate(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray(TOP_LEVEL_ARRAY_LABEL);
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            String date = singleMovieJson.getString(RELEASE_DATE_LABEL);

            SimpleDateFormat sdf  = new SimpleDateFormat(API_STRING_DATE_FORMAT, Locale.US);
            Date d = sdf.parse(date);
            Long unixTime = TimeUnit.MILLISECONDS.toMinutes(d.getTime());
            return String.valueOf(unixTime);
        } catch (JSONException | ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    static String getAuthor(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray(TOP_LEVEL_ARRAY_LABEL);
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString(AUTHOR_LABEL);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getId(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray(TOP_LEVEL_ARRAY_LABEL);
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString(ID_LABEL);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getMoviePlot(String movieJsonStr, int index) {
        try
        {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray(TOP_LEVEL_ARRAY_LABEL);
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString(PLOT_LABEL);
        } catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    static String getTrailerName(String trailerJsonStr, int index) {
        try {
            JSONObject trailerJson = new JSONObject(trailerJsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray(TOP_LEVEL_TRAILER_ARRAY_LABEL);
            JSONObject singleTrailer = trailerArray.getJSONObject(index);
            return singleTrailer.getString(TRAILER_NAME_LABEL);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getMovieAvgVotes(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray(TOP_LEVEL_ARRAY_LABEL);
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString(VOTE_AVG_LABEL);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getMovieTitle(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray(TOP_LEVEL_ARRAY_LABEL);
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString(TITLE_LABEL);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getMoviePosterPath(String movieJsonStr, int index) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray(TOP_LEVEL_ARRAY_LABEL);
            JSONObject singleMovieJson = moviesJson.getJSONObject(index);
            return singleMovieJson.getString(POSTER_PATH_LABEL);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static int getItemCount(String movieJsonStr, String topLevelLabel) {
        try{
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesJson = movieJson.getJSONArray(topLevelLabel);
            return moviesJson.length();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    static String getSingleMovieId(String movieJsonStr) {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            return movieJson.getString(ID_LABEL);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
