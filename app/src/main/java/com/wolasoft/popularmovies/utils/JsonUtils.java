package com.wolasoft.popularmovies.utils;

import android.util.Log;

import com.wolasoft.popularmovies.models.Movie;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();
    private static final String ORIGINAL_TITLE_KEY = "original_title";
    private static final String THUMBNAIL_URL_KEY = "poster_path";
    private static final String RATING_KEY = "vote_average";
    private static final String SYNOPSIS_KEY = "overview";
    private static final String RELEASE_DATE_KEY = "release_date";

    public static Movie parseMovieJson(String json) {
        Movie movie = null;
        try {
            JSONObject movieObject = new JSONObject(json);
            String originalTitle = movieObject.getString(ORIGINAL_TITLE_KEY);
            String thumbnailUrl = movieObject.getString(THUMBNAIL_URL_KEY);
            float rating = (float) movieObject.getDouble(RATING_KEY);
            String synopsis = movieObject.getString(SYNOPSIS_KEY);
            String releaseDate = movieObject.getString(RELEASE_DATE_KEY);

            movie = new Movie();
            movie.setOriginalTitle(originalTitle);
            movie.setThumbnailUrl(thumbnailUrl);
            movie.setRating(rating);
            movie.setSynopsis(synopsis);
            movie.setReleaseDate(releaseDate);
        } catch (JSONException error) {
            Log.e(TAG, error.toString());
        }

        return movie;
    }
}
