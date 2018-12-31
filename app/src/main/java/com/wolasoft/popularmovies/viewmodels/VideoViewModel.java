package com.wolasoft.popularmovies.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.wolasoft.popularmovies.data.repositories.MovieRepository;
import com.wolasoft.popularmovies.data.models.Video;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {

    private LiveData<List<Video>> videos;
    private final MovieRepository repository;

    public VideoViewModel(Application application) {
        super(application);
        this.repository = MovieRepository.getInstance(application.getApplicationContext());
    }

    public void init(int movieId) {
        this.videos = this.repository.getVideosFromApi(movieId);
    }

    public LiveData<List<Video>> getVideos() {
        return this.videos;
    }
}
