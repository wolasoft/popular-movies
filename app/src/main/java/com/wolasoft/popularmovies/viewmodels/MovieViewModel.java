package com.wolasoft.popularmovies.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.wolasoft.popularmovies.data.repositories.MovieRepository;
import com.wolasoft.popularmovies.data.models.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movies;
    private final MovieRepository repository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        this.repository = MovieRepository.getInstance(application.getApplicationContext());
    }

    public void init(String path) {
        this.movies = this.repository.getAllFromApi(path);
    }

    public LiveData<List<Movie>> getMovies() {
        return this.movies;
    }

    public LiveData<List<Movie>> getMoviesFromDb() {
        this.movies = this.repository.getAllFromDb();

        return movies;
    }
}
