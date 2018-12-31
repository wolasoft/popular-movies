package com.wolasoft.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wolasoft.popularmovies.adapters.MovieAdapter;
import com.wolasoft.popularmovies.data.models.Movie;
import com.wolasoft.popularmovies.databinding.ActivityMainBinding;
import com.wolasoft.popularmovies.utils.NetworkUtils;
import com.wolasoft.popularmovies.viewmodels.MovieViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickedListener {

    private final String MOVIES_LIST = "movies_list";
    private final String CURRENT_FILTER_TAG = "current_filter_tag";
    private String DEFAULT_FILTER = MOVIES_LIST;

    private ActivityMainBinding mainBinding;
    private MovieAdapter adapter;
    private List<Movie> movieList = null;

    private static final String MOST_POPULAR_MOVIES_PATH = "popular";
    private static final String TOP_RATED_MOVIES_PATH = "top_rated";
    private MovieViewModel movieViewModel;
    private int orientation;
    // private String currentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        orientation = getResources().getConfiguration().orientation;
        initViews();
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_FILTER_TAG)) {
            DEFAULT_FILTER = savedInstanceState.getString(CURRENT_FILTER_TAG);
            if (DEFAULT_FILTER == null) {
                showFavoriteMovies();
            } else if (DEFAULT_FILTER.equals(MOST_POPULAR_MOVIES_PATH)) {
                showMostPopularMovies();
            } else if (DEFAULT_FILTER.equals(TOP_RATED_MOVIES_PATH)){
                showHighestRatedMovies();
            }
        }
        else {
            showMostPopularMovies();
        }
    }

    private void initViews() {

        adapter = new MovieAdapter(this.movieList, this);
        mainBinding.moviesListRV.setAdapter(adapter);
        mainBinding.moviesListRV.setHasFixedSize(true);

        int NUMBER_OF_COLUMN;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            NUMBER_OF_COLUMN = 2;
        } else {
            NUMBER_OF_COLUMN = 3;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMN);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mainBinding.moviesListRV.setLayoutManager(layoutManager);
    }

    private void setupViewModel(@Nullable String path) {
        DEFAULT_FILTER = path;
        adapter.setDataChanged(null);
        hideErrorMessage();
        showProgressBar();
        mainBinding.emptyListMessageTV.setVisibility(View.GONE);

        if (path == null) {
            movieViewModel.getMoviesFromDb().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    updateUI(movies);
                }
            });
        } else if (NetworkUtils.isInternetAvailable(this)) {
            movieViewModel.init(path);
            movieViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    updateUI(movies);
                }
            });
        }
        else {
            showErrorMessage();
            hideProgressBar();
        }
    }

    private void updateUI(@Nullable List<Movie> movies) {
        movieList = movies;

        if (movieList == null) {
            showErrorMessage();
        }
        else if (movieList.size() <= 0){
            mainBinding.emptyListMessageTV.setVisibility(View.VISIBLE);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setDataChanged(movieList);
                hideProgressBar();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_FILTER_TAG, DEFAULT_FILTER);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        DEFAULT_FILTER = savedInstanceState.getString(CURRENT_FILTER_TAG, MOST_POPULAR_MOVIES_PATH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                showMostPopularMovies();
                break;
            case R.id.action_highest_rated:
                showHighestRatedMovies();
                break;
            case R.id.action_favorite_movies:
                showFavoriteMovies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void movieClicked(Movie movie) {
        Class movieDetailsActivityClass = MovieDetailsActivity.class;

        Intent movieDetailsIntent = new Intent(MainActivity.this, movieDetailsActivityClass);
        movieDetailsIntent.putExtra(MovieDetailsActivity.SELECTED_MOVIE, movie);
        startActivity(movieDetailsIntent);
    }

    private void showMostPopularMovies() {
        setupViewModel(MOST_POPULAR_MOVIES_PATH);
        setTitle(R.string.action_most_popular);
    }

    private void showHighestRatedMovies() {

        setupViewModel(TOP_RATED_MOVIES_PATH);
        setTitle(R.string.action_highest_rated);
    }


    private void showFavoriteMovies() {
        setupViewModel(null);
        setTitle(R.string.action_favorite);
    }

    private void showErrorMessage() {
        mainBinding.errorMessageTV.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage() {
        mainBinding.errorMessageTV.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        mainBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mainBinding.progressBar.setVisibility(View.GONE);
    }
}
