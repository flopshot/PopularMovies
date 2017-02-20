package com.example.sean.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.sean.popularmovies.data.MovieContract;

/**
 * This Fragment contains the main activity listView of Movie thumbnails
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private MovieThumbnailAdapter mMovieAdapter;
    private  MyObserver myObserver;
    private Uri movieAndFavoritesTableUri;
    private static final int MOVIE_LOADER = 0;
    private Boolean requestDataBoolean = null;
    private static LoaderManager.LoaderCallbacks sLoaderCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initiate Preference Manager for Async Task
        setHasOptionsMenu(true);
        // Observe Content Provider Data Changes
        myObserver = new MyObserver(new Handler());
        sLoaderCallback = this;
        // URI of Movie Table
        movieAndFavoritesTableUri = MovieContract.MovieEntry.buildMovieWithFavorites();
        // Register ContentObserver in onCreate to catch changes in detail fragment
        getActivity()
              .getContentResolver()
              .registerContentObserver(MovieContract.FavoritesEntry.CONTENT_URI, true, myObserver);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, sLoaderCallback);
        requestDataBoolean = ((MainActivity)getActivity()).requestDataBoolean;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_mainfrag, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (requestDataBoolean) {
            updateMovies();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getContentResolver().unregisterContentObserver(myObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMovieAdapter = new MovieThumbnailAdapter(getActivity().getApplicationContext(), null);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                   int position, long id) {

                Intent intent = new Intent();
                intent.setClassName(getActivity().getApplication()
                        ,DetailActivity.class.getName());

                intent.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_ID, String.valueOf(id));
                startActivity(intent);
            }
        });

        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            String idOfLongPressItem;
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_add_to_favorites:
                            ContentValues values = new ContentValues();
                            values.put(MovieContract.FavoritesEntry.COLUMN_MOVIE_ID, idOfLongPressItem);
                            getActivity().getContentResolver().insert(MovieContract.FavoritesEntry.CONTENT_URI, values);
                            values.clear();
                            return true;
                        case R.id.menu_remove_from_favorites:
                            getActivity().getContentResolver().delete(
                                  MovieContract.FavoritesEntry.CONTENT_URI,
                                  MovieContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?",
                                  new String[]{idOfLongPressItem});
                            return true;
                        default:
                            return false;
                    }
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                idOfLongPressItem = String.valueOf(id);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.movie_thumbnail_longpress, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                mode.setTitle(R.string.menu_favorites_title);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Context mContext = getActivity().getApplicationContext();
        String whereClause;
        String[] movieTypeListSettingQueryArgs = Utility
              .getPreferredMovieType(mContext);

        // WHERE clause filter only popular or top rated movies
        // for cursor to display in gridView of fragment
        if (movieTypeListSettingQueryArgs[0].equals(MovieContract.FavoritesEntry.COLUMN_MOVIE_ID)) {
            whereClause = movieTypeListSettingQueryArgs[0] + "<>0";
        } else {
            whereClause = movieTypeListSettingQueryArgs[0] + "=1";
        }
        // sort ORDER:  Descending, by popularity or vote score.
        String sortOrder = movieTypeListSettingQueryArgs[1] + " DESC";

        return new CursorLoader(mContext,
              movieAndFavoritesTableUri,
              null,
              whereClause,
              null,
              sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mMovieAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mMovieAdapter.swapCursor(null);
    }

    void updateMovies() {
        new FetchMoviesTask(getActivity().getApplicationContext()).execute();
    }

    public class MyObserver extends ContentObserver {
        // Consider Refactor to new class
        MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange,null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            getLoaderManager().restartLoader(MOVIE_LOADER, null, sLoaderCallback);
        }
    }
}