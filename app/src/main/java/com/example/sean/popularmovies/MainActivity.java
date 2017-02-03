package com.example.sean.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private Boolean requestDataBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("Main Activity1", String.valueOf(requestDataBoolean));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);

        //Setup a shared preference listener for hpwAddress and restart transport
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("sort")) {
                    intentRestart.setClassName(getApplication(), MainActivity.class.getName());
                    finish();
                    startActivity(intentRestart);
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listener);

        pref = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        if (pref != null) {
            Log.w("Main Activity2", String.valueOf(requestDataBoolean)); //TODO: Remove log tag
            requestDataBoolean = null;
            requestDataBoolean = pref.getBoolean(PREF_KEY, true);
            Log.w("Main Activity3", String.valueOf(requestDataBoolean));
        }

        if (savedInstanceState == null) {
            MovieFragment frag = new MovieFragment();
            Bundle args = new Bundle();
            args.putBoolean(PREF_KEY, requestDataBoolean);
            frag.setArguments(args);
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
        if (sortType.equals("1")) {
            sortType = "Popular Movies";
        }
        else{
            sortType = "Top Rated Movies";
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
