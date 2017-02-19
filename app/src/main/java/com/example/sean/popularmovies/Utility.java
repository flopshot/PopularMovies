package com.example.sean.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.sean.popularmovies.data.MovieContract;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


class Utility {
//    static HashMap getPreferredMovieType(Context context) {
//        HashMap<String, String> movieListQueryArgs = new HashMap<>();
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        String pref = prefs.getString(context.getString(R.string.pref_sort_key),
//              context.getString(R.string.pref_sort_toprated));
//        if (pref.equals("1")) {
//            movieListQueryArgs.put(MovieContract.MovieEntry.COLUMN_ISPOPOULAR, "1");
//            movieListQueryArgs.put(MovieContract.MovieEntry.COLUMN_ISTOPRATED, "0");
//            return  movieListQueryArgs;
//        } else {
//            movieListQueryArgs.put(MovieContract.MovieEntry.COLUMN_ISPOPOULAR, "0");
//            movieListQueryArgs.put(MovieContract.MovieEntry.COLUMN_ISTOPRATED, "1");
//            return  movieListQueryArgs;
//        }
//    }

    /**
     * @param context Application Context
     * @return String array of WHERE and SORT args for movie type in cursor query for displaying
     *          movies in the mvie fragment (Popular or Top Rated) based on preference settings
     */
    static String[] getPreferredMovieType(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultPref = context.getString(R.string.pref_sort_toprated);
        String popularSort = context.getString(R.string.pref_sort_popular);
        String favoriteSort = context.getString(R.string.pref_sort_favorites);
        String pref = prefs.getString(context.getString(R.string.pref_sort_key),
              defaultPref);
        if (pref.equals(defaultPref)) {
            return new String[] {MovieContract.MovieEntry.COLUMN_ISTOPRATED,
                  MovieContract.MovieEntry.COLUMN_AVG_VOTES};
        } else if (pref.equals(popularSort)) {
            return new String[] {MovieContract.MovieEntry.COLUMN_ISPOPOULAR,
                  MovieContract.MovieEntry.COLUMN_POPULARITY};
        } else if (pref.equals(favoriteSort)) {
            return new String[] {MovieContract.FavoritesEntry.COLUMN_MOVIE_ID,
                  MovieContract.MovieEntry.COLUMN_AVG_VOTES};
        } else {
            throw new RuntimeException(
                  "Error in Preferences: cannot load movies to gridView by preference settings"
            );
        }
    }

    static String getFormattedDate(Long unixtime) {
        Date date = new Date(unixtime*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        return sdf.format(date);
    }
    static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
