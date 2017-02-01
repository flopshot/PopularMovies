package com.example.sean.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This Fragment contains the main activity listView of Movie thumbnails
 */
public class MovieFragment extends Fragment {
    MovieThumbnailAdapter mMovieAdapter;
    MovieThumbnail[] movieThumbTup = {};
    List<MovieThumbnail> movieThumbList = new ArrayList<>(Arrays.asList(movieThumbTup));
    Intent intent = new Intent();
    Intent noNetworkIntent = new Intent();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initiate Preference Manager for Async Task
        setHasOptionsMenu(true);
        PreferenceManager.setDefaultValues(getActivity().getApplicationContext()
              ,R.xml.pref_settings
              ,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        new FetchMoviesTask(getActivity().getApplicationContext(), mMovieAdapter).execute();
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
            new FetchMoviesTask(getActivity().getApplicationContext(), mMovieAdapter).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // The arrayAdapter fils the gridLayout Items with MovieThumbnail Objects
        // containing a poster image and movie title

        mMovieAdapter = new MovieThumbnailAdapter(getActivity(),
                                                    movieThumbList);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(mMovieAdapter);

        // The itemClick listener starts the movie details
        // activity of the movie item that was clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view,
                                                                   int position, long id) {

                                                MovieThumbnail movie =
                                                      (MovieThumbnail) adapterView
                                                            .getItemAtPosition(position);
                                                intent.setClassName(getActivity().getApplication()
                                                        ,DetailActivity.class.getName());

                                                intent.putExtra("movie", movie);
                                                startActivity(intent);
                                            }
                                        }
        );
        return rootView;
    }

}
