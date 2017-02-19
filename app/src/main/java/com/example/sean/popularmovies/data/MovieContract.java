package com.example.sean.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
  * Defines table and column names for the movie database.
  */

public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    static final String CONTENT_AUTHORITY = "com.example.sean.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.sean.popularmovies/movie/ is a valid path for
    // looking at movie data. content://com.example.sean.popularmovies/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    static final String PATH_MOVIE = "movie";
    static final String PATH_TRAILER = "trailer";
    static final String PATH_REVIEW = "review";
    static final String PATH_FAVORITES = "favorites";

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
              BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        static final String CONTENT_TYPE =
              ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        static final String CONTENT_ITEM_TYPE =
              ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Table name
        public static final String TABLE_NAME = "movie";

        // The movie_id setting string is the movie id returned from api
        // & will be sent to server for the trailer and review queries.
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_AVG_VOTES = "avg_votes";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_ISPOPOULAR = "isPopular";
        public static final String COLUMN_ISTOPRATED = "isTopRated";

        static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieWithFavorites() {
            return CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
        }
    }

    /* Inner class that defines the table contents of the review table */
    public static final class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
              BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        static final String CONTENT_TYPE =
              ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        static final String CONTENT_ITEM_TYPE =
              ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        // Table name
        public static final String TABLE_NAME = "review";

        // The movie_id setting string is the movie id returned from api
        // & will be sent to server for the trailer and review queries.
        // The content column holds the review text and the url contains the
        // web page url that holds the review on the Movie DB website.
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";

        static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the trailer table */
    public static final class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
              BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        static final String CONTENT_TYPE =
              ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        static final String CONTENT_ITEM_TYPE =
              ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        // Table name
        public static final String TABLE_NAME = "trailer";

        // The movie_id setting string is the movie id returned from api
        // & will be sent to server for the trailer and review queries.
        // Key column is the video_id url parameter used to find YouTube
        // videos of our trailer
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_KEY = "key";

        static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the favorite movies table */
    public static final class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
              BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        static final String CONTENT_TYPE =
              ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;
        static final String CONTENT_ITEM_TYPE =
              ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        // Table name
        static final String TABLE_NAME = "favorites";

        // The movie_id setting string is the movie id returned from api
        // & will be sent to server for the trailer and review queries.
        public static final String COLUMN_MOVIE_ID = "favorite_movie_id";

        static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}


