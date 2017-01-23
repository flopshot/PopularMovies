package com.example.sean.popularmovies;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.res.Resources.NotFoundException;

/**
 * Boiler Plate class to request Movie DB data over the network. Returns JSON string from API
 * and delivers to Async Task
 */

class RequestMovieData {
    private Context mContext;
    private String APPID;
    private int developerApiId;

    RequestMovieData(Context c) {
        this.mContext = c;

        developerApiId = mContext
              .getResources()
              .getIdentifier("appid_local","string", mContext.getPackageName());

        if(developerApiId != 0) {
            this.APPID = mContext.getResources().getString(developerApiId);
        }
        else {
            this.APPID = mContext.getResources().getString(R.string.appid);
        }
    }

    String getMovieData(String sortPref){
        CheckNetworking networkCheck = new CheckNetworking(mContext);

        if(!networkCheck.haveNetworkConnection()) {
            return "No Network Connection";
        }

        String sortType;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        if (sortPref.equals("1")) {
            sortType = "popular";
        }
        else{
            sortType = "top_rated";
        }

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(sortType)
                .appendQueryParameter("api_key", APPID);

        String myUrl = builder.build().toString();

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            URL url =
                    new URL(myUrl);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();



        } catch (IOException e) {
            Log.e("RequestMovieData", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MovieFragment", "Error closing stream", e);
                }
            }
        }

        return movieJsonStr;
    }

}
