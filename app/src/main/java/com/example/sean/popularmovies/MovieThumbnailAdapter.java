package com.example.sean.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class MovieThumbnailAdapter extends ArrayAdapter<MovieThumbnail> {
    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param MovieThumbnails A List of MovieThumbnails objects to display in a list
     */
    MovieThumbnailAdapter(Activity context, List<MovieThumbnail> MovieThumbnails) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, MovieThumbnails);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the MovieThumbnail object from the ArrayAdapter at the appropriate position
        MovieThumbnail movieThumbnail = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }

        ImageView posterView = (ImageView) convertView.findViewById(R.id.grid_item_movie_poster);
        Picasso.with(getContext())
                .load(movieThumbnail.movieImage)
                .placeholder(R.drawable.movie_icon)
                .error(R.drawable.movie_icon)
                .into(posterView);

        // We use Picasso library to handle image files for movie posters. This library
        // caches images and handles worker threads for downloading images.

        // The grid view item holds a poster and a movie title view. We write the movie title
        // to the textVew befrore returning the item view
        TextView titleView = (TextView) convertView.findViewById(R.id.grid_item_movie_title);
        titleView.setText(movieThumbnail.movieTitle);

        return convertView;
    }
}
