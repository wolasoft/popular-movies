package com.wolasoft.popularmovies.data.api.dto;

import com.google.gson.annotations.SerializedName;
import com.wolasoft.popularmovies.data.models.Movie;

import java.util.List;

public class MovieDTO {
    @SerializedName("results")
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }
}
