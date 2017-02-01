package com.example.sean.popularmovies.data.api;

import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Describes the request URL parts. If the server, api,
 * or data source changes, we can update this single source
 * to reflect the change in the URL with robustness.
 */
// TODO: Create Unit Test
public class ApiContract {
    private static final String SCHEME = "https";
    private static final String AUTHORITY = "api.themoviedb.org";
    private static final String[] PATH = {"3","movie"};
    private static final String PARAM = "api_key";
    private static final String[] QUERY_TYPE = {"popular", "top_rated", "trailers", "reviews"};

    /**
     * @param api : String of api key value
     * @param queryType : Intetger of query type (0: Popular Movies, 1: Top Rated Movies
     *                      2: Trailers of a specific movie, 3: Reviews of a specific movie
     * @param movieId : movie_id to be used in trailer/review queries. Null if query is popular/top
     * @return request URL string for the JSON data of movies
     */
    public static String buildRequestUrl(String api, int queryType, @Nullable String movieId) {
        final int POPULAR = 0;
        final int TOP_RATED = 1;
        final int TRAILERS = 2;
        final int REVIEWS = 3;

        //Check if query type is valid
        if (queryType < 0 || queryType > 3) {
            throw new RuntimeException("Invalid queryType: See api/ApiContract.java");
        }

        // Build up the request URL piece by piece
        // "https://api.themoviedb.org"
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
              .authority(AUTHORITY);
        // "https://api.themoviedb.org/3/movie"
        for (String path : PATH) {
            builder.appendPath(path);
        }

        switch (queryType) {
            case POPULAR: { // "https://api.themoviedb.org/3/movie/popular"
                builder.appendPath(QUERY_TYPE[queryType]);
                break;
            }
            case TOP_RATED: { // "https://api.themoviedb.org/3/movie/top_rated"
                builder.appendPath(QUERY_TYPE[queryType]);
                break;
            }
            case TRAILERS: { // "https://api.themoviedb.org/3/movie/<MOVIE_ID>/trailers"
                builder.appendPath(movieId);
                builder.appendPath(QUERY_TYPE[queryType]);
                break;
            }
            case REVIEWS: { // "https://api.themoviedb.org/3/movie/<MOVIE_ID>/reviews"
                builder.appendPath(movieId);
                builder.appendPath(QUERY_TYPE[queryType]);
                break;
            }
        }
        // "?api_key=<API_KEY>"
        builder.appendQueryParameter(PARAM, api);
        return builder.build().toString();
    }
}
