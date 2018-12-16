package com.wolasoft.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wolasoft.popularmovies.adapters.MovieAdapter;
import com.wolasoft.popularmovies.models.Movie;
import com.wolasoft.popularmovies.utils.HttpUtils;
import com.wolasoft.popularmovies.utils.JsonUtils;
import com.wolasoft.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickedListener {

    private final String MOVIES_LIST = "movies_list";

    private TextView errorMessageTv;
    private TextView emptyListMessageTv;
    private ProgressBar progressBar;
    private MovieAdapter adapter;
    private List<Movie> movieList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView moviesListRv = findViewById(R.id.movies_list_rv);
        emptyListMessageTv = findViewById(R.id.empty_list_message_tv);
        errorMessageTv = findViewById(R.id.error_message_tv);
        progressBar = findViewById(R.id.progress_bar);

        adapter = new MovieAdapter(this.movieList, this);
        moviesListRv.setAdapter(adapter);
        moviesListRv.setHasFixedSize(true);

        int NUMBER_OF_COLUMN = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMN);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        moviesListRv.setLayoutManager(layoutManager);

        if (savedInstanceState != null) {
            this.movieList = savedInstanceState.getParcelableArrayList(MOVIES_LIST);
            adapter.setDataChanged(this.movieList);
        }
        else {
            showMostPopularMovies();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(MOVIES_LIST, new ArrayList<>(this.movieList));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        this.movieList = savedInstanceState.getParcelableArrayList(MOVIES_LIST);
        adapter.setDataChanged(this.movieList);
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
        String MOST_POPULAR_MOVIES_PATH = "popular";
        new HttpAsyncTask().execute(MOST_POPULAR_MOVIES_PATH);
        setTitle(R.string.action_most_popular);
    }

    private void showHighestRatedMovies() {
        String TOP_RATED_MOVIES_PATH = "top_rated";
        new HttpAsyncTask().execute(TOP_RATED_MOVIES_PATH);
        setTitle(R.string.action_highest_rated);
    }

    private void showErrorMessage() {
        this.errorMessageTv.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage() {
        this.errorMessageTv.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        this.progressBar.setVisibility(View.GONE);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            adapter.setDataChanged(null);
            hideErrorMessage();
            showProgressBar();
            emptyListMessageTv.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            String response = null;
            if (NetworkUtils.isInternetAvailable(MainActivity.this)) {
                response = HttpUtils.requestApi(path);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                movieList = JsonUtils.parseMovieJson(response);
            }

            if (movieList == null) {
                showErrorMessage();
            }
            else if (movieList.size() <= 0){
                emptyListMessageTv.setVisibility(View.VISIBLE);
            }

            adapter.setDataChanged(movieList);
            hideProgressBar();
        }
    }
}
