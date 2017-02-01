package com.example.sean.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Movie Detail Activity contains Activity logic for movie in list item that was clicked in
 * Movie Fragment. Contains a detail Fragment class to display movie details layout
 */
public class DetailActivity extends AppCompatActivity {

    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intentDetail = getIntent();
        MovieThumbnail movie = intentDetail.getParcelableExtra("movie");

        super.onCreate(savedInstanceState);

        //Start detail fragment for movie detail layout
        DetailFragment detailFragment = new DetailFragment();
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putParcelable("movieDetail", movie);
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_detail, detailFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            intent.setClassName(getApplication()
                    , SettingsActivity.class.getName());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
