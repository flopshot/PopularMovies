package com.example.sean.popularmovies;
import java.util.ArrayList;
import java.util.List;

class MovieArrayCreator {
    static List<MovieThumbnail> getMovieArray(String json){
        MovieDataParser movieDataParser = new MovieDataParser();
        List<MovieThumbnail> movieArray = new ArrayList<>();

        int loopMax = movieDataParser.getMovieCount(json);

        for (int i = 0; i < loopMax; i++) {
            String title = movieDataParser.getMovieTitle(json, i);
            String plot = movieDataParser.getMoviePlot(json, i);

            String avgVoteRaw = movieDataParser.getMovieAvgVotes(json, i);
            String avgVote = avgVoteRaw.substring(0, Math.min(avgVoteRaw.length(),3)) + " / 10";

            String poster_path = movieDataParser.getMoviePosterPath(json, i);
            String poster_uri = "https://image.tmdb.org/t/p/w500" +  poster_path;

            String releaseDate =  movieDataParser.getMovieReleaseDate(json, i);

            movieArray.add(new MovieThumbnail(title, poster_uri, releaseDate, avgVote, plot));
        }

        return movieArray;
    }
}