package com.wolasoft.popularmovies.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.wolasoft.popularmovies.data.api.ApiConnector;
import com.wolasoft.popularmovies.data.api.dto.MovieDTO;
import com.wolasoft.popularmovies.data.api.dto.ReviewDTO;
import com.wolasoft.popularmovies.data.api.dto.VideoDTO;
import com.wolasoft.popularmovies.data.api.services.MovieService;
import com.wolasoft.popularmovies.data.database.AppDatabase;
import com.wolasoft.popularmovies.data.database.MovieDao;
import com.wolasoft.popularmovies.data.models.Movie;
import com.wolasoft.popularmovies.data.models.Review;
import com.wolasoft.popularmovies.data.models.Video;
import com.wolasoft.popularmovies.utils.ExecutorUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private static final String TAG = "MovieRepository";
    private static MovieRepository instance = null;
    private final MovieService service;
    private final MovieDao movieDao;

    private MovieRepository(Context context, MovieService service) {
        this.service = service;
        this.movieDao = AppDatabase.getInstance(context).movieDao();
    }

    public static synchronized MovieRepository getInstance(Context context) {
        if (instance == null)
            instance = new MovieRepository(context, ApiConnector.createRetrofitService(MovieService.class));
        return instance;
    }

    public LiveData<List<Movie>> getAllFromApi(String path) {
        final MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        Call call = service.getAll(path);
        Log.d(TAG, "Fetching movies");
        call.enqueue(new Callback<List<Movie>>() {

            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    MovieDTO movieDTO = (MovieDTO) response.body();
                    data.setValue(movieDTO.getMovies());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<List<Review>> getReviewsFromApi(int movieId) {
        final MutableLiveData<List<Review>> data = new MutableLiveData<>();
        Call call = service.getReviews(movieId);
        Log.d(TAG, "Fetching movie reviews");
        call.enqueue(new Callback<List<Review>>() {

            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful()) {
                    ReviewDTO reviewDTO = (ReviewDTO) response.body();
                    data.setValue(reviewDTO.getReviews());
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<List<Video>> getVideosFromApi(int movieId) {
        final MutableLiveData<List<Video>> data = new MutableLiveData<>();
        Call call = service.getVideos(movieId);
        Log.d(TAG, "Fetching movie videos");
        call.enqueue(new Callback<List<Video>>() {

            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    VideoDTO videoDTO = (VideoDTO) response.body();
                    data.setValue(videoDTO.getVideos());
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<List<Movie>> getAllFromDb() {
        return movieDao.getAll();
    }

    public void save(final Movie movie) {
        ExecutorUtils.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                movieDao.insert(movie);
            }
        });
    }

    public LiveData<Movie> getById(int movieId) {
        return movieDao.getById(movieId);
    }

    public void delete(final Movie movie) {
        ExecutorUtils.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                movieDao.delete(movie);
            }
        });
    }
}
