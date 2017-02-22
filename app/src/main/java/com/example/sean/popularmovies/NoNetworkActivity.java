package com.example.sean.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Simple activity to display "No Network" message when device has no network connectivity
 */

public class NoNetworkActivity extends AppCompatActivity {
    Intent intentMain = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mainfrag, menu);
        return true;
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            intentMain.setClassName(getApplication()
                    ,MainActivity.class.getName());
            intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                  | Intent.FLAG_ACTIVITY_NEW_TASK
                  | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intentMain);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
