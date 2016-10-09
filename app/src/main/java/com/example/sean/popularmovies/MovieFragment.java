package com.example.sean.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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


public class MovieFragment extends Fragment {
    MovieThumbnailAdapter mMovieAdapter;
    MovieThumbnail[] movieThumbTup = {};
    List<MovieThumbnail> movieThumbList = new ArrayList<>(Arrays.asList(movieThumbTup));
    Intent intent = new Intent();
    Intent noNetworkIntent = new Intent();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle detail events.
        setHasOptionsMenu(true);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_settings, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        new FetchMoviesTask().execute();
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
            new FetchMoviesTask().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMovieAdapter = new MovieThumbnailAdapter(getActivity(),
                                                    movieThumbList);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view,
                                                                   int position, long id) {

                                                MovieThumbnail movie = (MovieThumbnail) adapterView.getItemAtPosition(position);
                                                intent.setClassName(getActivity().getApplication()
                                                        ,DetailActivity.class.getName());

                                                intent.putExtra("movie", movie);
                                                startActivity(intent);
                                            }
                                        }
        );
        return rootView;
    }

    class FetchMoviesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... args) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortPref = sharedPref.getString("sort", "");

            RequestMovieData movies = new RequestMovieData(getActivity());
            return movies.getMovieData(sortPref);
        }


        @Override
        protected void onPostExecute(String jsonMovieData) {
            if (jsonMovieData.equals("No Network Connection")){
                noNetworkIntent.setClassName(getActivity().getApplication()
                        ,NoNetworkActivity.class.getName()
                );

                startActivity(noNetworkIntent);
            }
            else {
                List<MovieThumbnail> movieArray = MovieArrayCreator
                        .getMovieArray(jsonMovieData
                        );

                mMovieAdapter.clear();
                mMovieAdapter.addAll(movieArray);
                mMovieAdapter.notifyDataSetChanged();
            }
        }
    }
}
