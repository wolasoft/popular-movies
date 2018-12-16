package com.wolasoft.popularmovies.utils;

import android.util.Log;

import com.wolasoft.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();
    private static final String MOVIES_LIST_KEY = "results";
    private static final String ORIGINAL_TITLE_KEY = "original_title";
    private static final String THUMBNAIL_URL_KEY = "poster_path";
    private static final String RATING_KEY = "vote_average";
    private static final String SYNOPSIS_KEY = "overview";
    private static final String RELEASE_DATE_KEY = "release_date";
    private static final String FALLBACK_STRING_VALUE = "Unknown";

    public static List<Movie> parseMovieJson(String json) {
        List<Movie> moviesList = null;
        Movie movie;
        try {
            JSONObject moviesListObject = new JSONObject(json);
            JSONArray moviesArray = moviesListObject.getJSONArray(MOVIES_LIST_KEY);
            moviesList = new ArrayList<>();

            for (int i=0; i< moviesArray.length(); i++) {
                String movieJson = moviesArray.get(i).toString();
                JSONObject movieObject = new JSONObject(movieJson);

                String originalTitle = movieObject.optString(ORIGINAL_TITLE_KEY, FALLBACK_STRING_VALUE);
                String thumbnailUrl = movieObject.optString(THUMBNAIL_URL_KEY, FALLBACK_STRING_VALUE);
                float rating = (float) movieObject.optDouble(RATING_KEY, 0);
                String synopsis = movieObject.optString(SYNOPSIS_KEY, FALLBACK_STRING_VALUE);
                String releaseDate = movieObject.optString(RELEASE_DATE_KEY, FALLBACK_STRING_VALUE);

                movie = new Movie();
                movie.setOriginalTitle(originalTitle);
                movie.setThumbnailUrl(thumbnailUrl);
                movie.setRating(rating);
                movie.setSynopsis(synopsis);
                movie.setReleaseDate(releaseDate);

                moviesList.add(movie);
            }

        } catch (JSONException error) {
            Log.e(TAG, error.toString());
        }

        return moviesList;
    }
}
