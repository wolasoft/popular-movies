package com.wolasoft.popularmovies.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.wolasoft.popularmovies.data.repositories.MovieRepository;
import com.wolasoft.popularmovies.data.models.Review;

import java.util.List;

public class ReviewViewModel extends AndroidViewModel {

    private LiveData<List<Review>> reviews;
    private final MovieRepository repository;

    public ReviewViewModel(Application application) {
        super(application);
        this.repository = MovieRepository.getInstance(application.getApplicationContext());
    }

    public void init(int movieId) {
        this.reviews = this.repository.getReviewsFromApi(movieId);
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }
}
