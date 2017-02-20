package com.example.sean.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * For the Popular Movies app Stage 1, we create the main activity which contains one
 * fragment. This activity holds 2 intents which start the Settings Activity (from
 * the settings button in the actionbar menu) and the MovieDetail activity. We also
 * add setting preferences logic to obtain user settings.
 */
public class MainActivity extends AppCompatActivity {
    Intent intentSettings = new Intent();
    Intent intentRestart = new Intent();
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    SharedPreferences prefs, pref = null;
    private static final String PREF_KEY = "requestNetworkData";
    Boolean requestDataBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);

        //Setup a shared preference listener for hpwAddress and restart transport
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("sort")) {
                    intentRestart.setClassName(getApplication(), MainActivity.class.getName());
                    intentRestart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intentRestart);
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listener);

        // Check if the activity called isFinishing() in the last onPause() call.
        // If it did not, don't update the movie data from the movie DB server.
        // If it did, then refresh the movie data over the network through an api call
        //      by setting "requestDataBoolean" to true.
        // The fragment will read this variable and call "updateMovies()"
        pref = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        if (pref != null) {
            requestDataBoolean = null;
            requestDataBoolean = pref.getBoolean(PREF_KEY, true);
        }

        // Create Fragment
        if (savedInstanceState == null) {
            MovieFragment frag = new MovieFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, frag)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Overriding onStart to display sort type in title through shared preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortType =  sharedPref.getString("sort", "");
        switch (sortType) {
            case "1":
                sortType = "Popular Movies";
                break;
            case "0":
                sortType = "Top Rated Movies";
                break;
            case "2":
                sortType = "Favorite Movies";
                break;
            default:
                break;
        }
        setTitle(sortType);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            intentSettings.setClassName(getApplication()
                    ,SettingsActivity.class.getName());
            intentSettings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intentSettings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pref = getSharedPreferences(PREF_KEY,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_KEY, isFinishing());
        editor.apply();
    }
}