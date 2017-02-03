package com.example.sean.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.sean.popularmovies.data.MovieContract;
/**
 * This Fragment contains the main activity listView of Movie thumbnails
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    MovieThumbnailAdapter mMovieAdapter;
    private static final int MOVIE_LOADER = 0;
    private boolean requestDataBoolean;
    private static final String PREF_KEY = "requestNetworkData";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestDataBoolean = this.getArguments().getBoolean(PREF_KEY);
        // Initiate Preference Manager for Async Task
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
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
        Log.w("Movie Frag", String.valueOf(requestDataBoolean)); //TODO: Remove log tag
        if (requestDataBoolean) {
            updateMovies();
        }
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
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] movieTypeListSettingQueryArgs = Utility.
              getPreferredMovieType(getActivity().getApplicationContext());

        // WHERE clause filter only popular or top rated movies
        // for cursor to display in gridview of fragment
        String whereClause = movieTypeListSettingQueryArgs[0] + "=1";
        // sort ORDER:  Descending, by popularity or vote score.
        String sortOrder = movieTypeListSettingQueryArgs[1] + " DESC";
        // URI of Movie Table
        Uri movieTableUri = MovieContract.MovieEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
              movieTableUri,
              null,
              whereClause,
              null,
              sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mMovieAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mMovieAdapter.swapCursor(null);
    }

    void updateMovies() {
        Log.w("MovieFragment", "Update Movies is Running");
        new FetchMoviesTask(getActivity().getApplicationContext()).execute();
    }
}
