package com.example.sean.popularmovies.data.api;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.sean.popularmovies.CheckNetworking;
import com.example.sean.popularmovies.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Boiler Plate class to request Movie DB data over the network. Returns JSON string from API
 * and delivers to Async Task
 */

public class RequestMovieData {
    private Context mContext;
    private String APPID;

    public RequestMovieData(Context c) {
        int developerApi;
        this.mContext = c;

        // This block of code sets the API key from a resource file. If you are not the
        // developer put your api key in the api_key.xml file of the res/values directory
        developerApi = mContext
              .getResources()
              .getIdentifier("appid_local","string", mContext.getPackageName());

        if(developerApi != 0) {
            this.APPID = mContext.getResources().getString(developerApi);
        } else {
            this.APPID = mContext.getResources().getString(R.string.appid);
        }
    }

    public String getMovieData(int queryType, @Nullable String movieId){
        //If there is no network connectivity, return the appropriate message
        CheckNetworking networkCheck = new CheckNetworking(mContext);
        if(!networkCheck.hasNetworkConnection()) {
            return "No Network Connection";
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        // Construct the URL for the MovieDB query
        // Possible parameters are avaiable at Movie DB API page, at
        // https://developers.themoviedb.org/3/getting-started
        String urlRequestQuery = ApiContract.buildRequestUrl(APPID, queryType, movieId);
        try {
            URL url = new URL(urlRequestQuery);

            // Create the request to Server, and open the connection
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
            // If the code didn't successfully get the movie data, there's no point in attempting
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
                }
            }
        }
        return movieJsonStr;
    }
}