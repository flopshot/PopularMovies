package com.example.sean.popularmovies.data;

import android.net.Uri;

import com.example.sean.popularmovies.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)

public class TestMovieContract {
    private static final String TEST_ID = "42";
    private static final String TEST_CONTENT_AUTHORITY = "com.example.sean.popularmovies";
    private static final String TEST_MOVIE = "movie";
    private static final String TEST_TRAILER = "trailer";
    private static final String TEST_REVIEW = "review";
    private static final String TEST_FAVORITES = "favorites";
    private static final Uri TEST_BASE_CONTENT_URI = Uri.parse("content://" + TEST_CONTENT_AUTHORITY);

    private static final Uri TEST_MOVIE_CONTENT_URI = Uri.parse("content://" + TEST_CONTENT_AUTHORITY + "/" + TEST_MOVIE);
    private static final Uri TEST_TRAILER_CONTENT_URI = Uri.parse("content://" + TEST_CONTENT_AUTHORITY + "/" + TEST_TRAILER);
    private static final Uri TEST_REVIEW_CONTENT_URI = Uri.parse("content://" + TEST_CONTENT_AUTHORITY + "/" + TEST_REVIEW);
    private static final Uri TEST_FAVORITE_CONTENT_URI = Uri.parse("content://" + TEST_CONTENT_AUTHORITY + "/" + TEST_FAVORITES);

    private static final String TEST_MOVIE_CONTENT_TYPE = "vnd.android.cursor.dir/" + TEST_CONTENT_AUTHORITY + "/" + TEST_MOVIE;
    private static final String TEST_MOVIE_ITEM_TYPE = "vnd.android.cursor.item/" + TEST_CONTENT_AUTHORITY + "/" + TEST_MOVIE;
    private static final String TEST_REVIEW_CONTENT_TYPE = "vnd.android.cursor.dir/" + TEST_CONTENT_AUTHORITY + "/" + TEST_REVIEW;
    private static final String TEST_REVIEW_ITEM_TYPE = "vnd.android.cursor.item/" + TEST_CONTENT_AUTHORITY + "/" + TEST_REVIEW;
    private static final String TEST_TRAILER_CONTENT_TYPE = "vnd.android.cursor.dir/" + TEST_CONTENT_AUTHORITY + "/" + TEST_TRAILER;
    private static final String TEST_TRAILER_ITEM_TYPE = "vnd.android.cursor.item/" + TEST_CONTENT_AUTHORITY + "/" + TEST_TRAILER;
    private static final String TEST_FAVORITE_CONTENT_TYPE = "vnd.android.cursor.dir/" + TEST_CONTENT_AUTHORITY + "/" + TEST_FAVORITES;
    private static final String TEST_FAVORITE_ITEM_TYPE = "vnd.android.cursor.item/" + TEST_CONTENT_AUTHORITY + "/" + TEST_FAVORITES;

    private static final Uri TEST_MOVIE_BUILD_URI = Uri.parse("content://" + TEST_CONTENT_AUTHORITY + "/" + TEST_MOVIE + "/" + TEST_ID);
    private static final Uri TEST_TRAILER_BUILD_URI = Uri.parse("content://" + TEST_CONTENT_AUTHORITY + "/" + TEST_TRAILER + "/" + TEST_ID);
    private static final Uri TEST_REVIEW_BUILD_URI = Uri.parse("content://" + TEST_CONTENT_AUTHORITY + "/" + TEST_REVIEW + "/" + TEST_ID);
    private static final Uri TEST_FAVORITE_BUILD_URI = Uri.parse("content://" + TEST_CONTENT_AUTHORITY + "/" + TEST_FAVORITES + "/" + TEST_ID);

    @Test
    public void testMovieContractUri() throws Exception {
        MovieContract contract = new MovieContract();
        assertEquals(TEST_BASE_CONTENT_URI, contract.BASE_CONTENT_URI);
    }
    @Test
    public void testMovieEntry() throws Exception {
        MovieContract.MovieEntry movEntry = new MovieContract.MovieEntry();

        assertEquals(TEST_MOVIE_CONTENT_URI, movEntry.CONTENT_URI);
        assertEquals(TEST_MOVIE_CONTENT_TYPE, movEntry.CONTENT_TYPE);
        assertEquals(TEST_MOVIE_ITEM_TYPE, movEntry.CONTENT_ITEM_TYPE);
        assertEquals(TEST_MOVIE_BUILD_URI, movEntry.buildMovieUri(42));
    }
    @Test
    public void testReviewEntry() throws Exception {
        MovieContract.ReviewEntry revEntry = new MovieContract.ReviewEntry();

        assertEquals(TEST_REVIEW_CONTENT_URI, revEntry.CONTENT_URI);
        assertEquals(TEST_REVIEW_CONTENT_TYPE, revEntry.CONTENT_TYPE);
        assertEquals(TEST_REVIEW_ITEM_TYPE, revEntry.CONTENT_ITEM_TYPE);
        assertEquals(TEST_REVIEW_BUILD_URI, revEntry.buildReviewUri(42));
    }
    @Test
    public void testTrailerEntry() throws Exception {
        MovieContract.TrailerEntry traEntry = new MovieContract.TrailerEntry();

        assertEquals(TEST_TRAILER_CONTENT_URI, traEntry.CONTENT_URI);
        assertEquals(TEST_TRAILER_CONTENT_TYPE, traEntry.CONTENT_TYPE);
        assertEquals(TEST_TRAILER_ITEM_TYPE, traEntry.CONTENT_ITEM_TYPE);
        assertEquals(TEST_TRAILER_BUILD_URI, traEntry.buildTrailerUri(42));
    }

    @Test
    public void testFavoritesEntry() throws Exception {
        MovieContract.FavoritesEntry favEntry = new MovieContract.FavoritesEntry();

        assertEquals(TEST_FAVORITE_CONTENT_URI, favEntry.CONTENT_URI);
        assertEquals(TEST_FAVORITE_CONTENT_TYPE, favEntry.CONTENT_TYPE);
        assertEquals(TEST_FAVORITE_ITEM_TYPE, favEntry.CONTENT_ITEM_TYPE);
        assertEquals(TEST_FAVORITE_BUILD_URI, favEntry.buildTrailerUri(42));
    }
}
