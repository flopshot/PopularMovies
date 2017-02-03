package com.example.sean.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sean.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

class MovieThumbnailAdapter extends CursorAdapter {
    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param c              A Cursor of Movie data from app content provider
     */
    MovieThumbnailAdapter(Context context, Cursor c) {
        super(context, c, 0);
        // Here, we initialize the CursorAdapter's internal storage for the context and the list.
        // the third argument is used when the CursorAdapter is registering a content observer
        // listener, which is not needed when using a cursorloader. Here, we used 0.
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.grid_item_movie, parent, false);
    }

    /*
    This is where we fill-in the views with the contents of the cursor.
    */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.
        ImageView posterView = (ImageView) view.findViewById(R.id.grid_item_movie_poster);

        int posterPathIdx = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        String posterPathStr = cursor.getString(posterPathIdx);
        Picasso.with(context)
              .load(posterPathStr)
              .placeholder(R.drawable.movie_icon)
              .error(R.drawable.movie_icon)
              .into(posterView);

        // We use Picasso library to handle image files for movie posters. This library
        // caches images and handles worker threads for downloading images.

        // The grid view item holds a poster and a movie title view. We write the movie title
        // to the textVew before returning the item view
        TextView titleView = (TextView) view.findViewById(R.id.grid_item_movie_title);
        int movieTitleIdx = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        titleView.setText(cursor.getString(movieTitleIdx));
    }

    /**
     * @param position : integer position of the item clicked in the gridview
     * @return Custom item id, corresponding to the movie_id in the database
     */
    @Override
    public long getItemId(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
    }
}