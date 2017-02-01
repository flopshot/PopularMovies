package com.example.sean.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**Implementing Parcelable class to MovieThumbnail object
 * to persist as bundle for passing to fragments
 */
class MovieThumbnail implements Parcelable{

    String movieTitle;
    String movieImage;
    String movieReleaseDate;
    String movieAvgVotes;
  //  String movieCat;
    String moviePlot;

    //Keeping logic to include movie category
    MovieThumbnail(String title, String image, String releaseDate, //String cat,
                          String avgVotes, String plot)
    {
        this.movieTitle = title;
        this.movieImage = image;
        this.movieReleaseDate = releaseDate;
        //    this.movieCat = cat;
        this.movieAvgVotes = avgVotes;
        this.moviePlot = plot;
    }

    //Private Constructor for internal CREATOR object for self initializing
    // once initialized from bundle from
    private MovieThumbnail(Parcel in){
        movieTitle = in.readString();
        movieImage = in.readString();
        movieReleaseDate = in.readString();
        //   movieCat = in.readString();
        movieAvgVotes = in.readString();
        moviePlot = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieTitle);
        parcel.writeString(movieImage);
        parcel.writeString(movieReleaseDate);
     //   parcel.writeString(movieCat);
        parcel.writeString(movieAvgVotes);
        parcel.writeString(moviePlot);
    }

    // Methods createFromParcel is the custom, internal
    // constructor method that uses private constructor
    // newArray method persist parcelables in arrays
    public static final Parcelable.Creator<MovieThumbnail> CREATOR = new Parcelable.Creator<MovieThumbnail>() {
        @Override
        public MovieThumbnail createFromParcel(Parcel parcel) {
            return new MovieThumbnail(parcel);
        }

        @Override
        public MovieThumbnail[] newArray(int size) {
            return new MovieThumbnail[size];
        }

    };

}
