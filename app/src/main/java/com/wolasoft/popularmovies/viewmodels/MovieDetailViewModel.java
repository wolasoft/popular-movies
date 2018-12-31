package com.wolasoft.popularmovies.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.wolasoft.popularmovies.data.models.Movie;
import com.wolasoft.popularmovies.data.repositories.MovieRepository;

public class MovieDetailViewModel extends ViewModel {

    private final LiveData<Movie> movie;

    public MovieDetailViewModel(@NonNull MovieRepository repository, int movieId) {
        this.movie = repository.getById(movieId);
    }

    public LiveData<Movie> getMovie() {
        return this.movie;
    }
}
