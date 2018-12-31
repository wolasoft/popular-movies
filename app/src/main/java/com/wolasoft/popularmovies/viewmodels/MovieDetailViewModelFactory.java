package com.wolasoft.popularmovies.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wolasoft.popularmovies.data.repositories.MovieRepository;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MovieRepository repository;
    private final int movieId;

    public MovieDetailViewModelFactory(MovieRepository repository, int movieId) {
        this.repository = repository;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailViewModel(this.repository, this.movieId);
    }
}
