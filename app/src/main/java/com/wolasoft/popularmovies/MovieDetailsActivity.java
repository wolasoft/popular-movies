package com.wolasoft.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wolasoft.popularmovies.adapters.ReviewAdapter;
import com.wolasoft.popularmovies.adapters.VideoAdapter;
import com.wolasoft.popularmovies.data.models.Movie;
import com.wolasoft.popularmovies.data.models.Review;
import com.wolasoft.popularmovies.data.models.Video;
import com.wolasoft.popularmovies.data.repositories.MovieRepository;
import com.wolasoft.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.wolasoft.popularmovies.utils.Constants;
import com.wolasoft.popularmovies.utils.ExecutorUtils;
import com.wolasoft.popularmovies.utils.NetworkUtils;
import com.wolasoft.popularmovies.viewmodels.MovieDetailViewModel;
import com.wolasoft.popularmovies.viewmodels.MovieDetailViewModelFactory;
import com.wolasoft.popularmovies.viewmodels.ReviewViewModel;
import com.wolasoft.popularmovies.viewmodels.VideoViewModel;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class MovieDetailsActivity extends AppCompatActivity implements VideoAdapter.OnVideoClickedListener {

    public static final String SELECTED_MOVIE = "selected_movie";
    private static final String YOUTUBE_APP = "vnd.youtube";
    private static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    private ActivityMovieDetailsBinding mainBinding;
    private VideoViewModel videoViewModel;
    private ReviewViewModel reviewViewModel;
    private MenuItem favoriteMenu;

    private Movie movie;
    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;
    private boolean isMovieSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        Intent intent = getIntent();

        if(savedInstanceState != null && savedInstanceState.containsKey(SELECTED_MOVIE)) {
            movie = savedInstanceState.getParcelable(SELECTED_MOVIE);
            if (movie != null) {
                showPersistenceState(movie);
                populateUI(movie);
            }
        } else if (intent.hasExtra(SELECTED_MOVIE)) {
            movie = intent.getParcelableExtra(SELECTED_MOVIE);
            if (movie != null) {
                showPersistenceState(movie);
                populateUI(movie);
            }
        }

        initViews();

        videoViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        reviewViewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);
        loadTrailers();
        loadReviews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (movie != null) {
            outState.putParcelable(SELECTED_MOVIE, movie);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        this.movie = savedInstanceState.getParcelable(SELECTED_MOVIE);
        if (this.movie != null) {
            populateUI(movie);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        favoriteMenu = menu.findItem(R.id.action_favorite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_favorite:
                String message = isMovieSaved ? getString(R.string.message_movie_unsaved)
                        : getString(R.string.message_movie_saved);
                Toast mToast = Toast.makeText(this, message, LENGTH_SHORT);
                mToast.show();
                saveMovie();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        changeFavoriteIcon();
        return super.onPrepareOptionsMenu(menu);
    }

    private void changeFavoriteIcon() {
        if (isMovieSaved) {
            favoriteMenu.setIcon(R.drawable.ic_star_24dp);

        } else {
            favoriteMenu.setIcon(R.drawable.ic_star_border_24dp);
        }
    }

    private void initViews() {
        videoAdapter = new VideoAdapter(null, this);
        mainBinding.trailerRV.setAdapter(videoAdapter);
        LinearLayoutManager videosLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainBinding.trailerRV.setLayoutManager(videosLayoutManager);
        mainBinding.trailerRV.setHasFixedSize(true);

        reviewAdapter = new ReviewAdapter(null);
        mainBinding.reviewRV.setAdapter(reviewAdapter);
        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainBinding.reviewRV.setLayoutManager(reviewsLayoutManager);
        mainBinding.reviewRV.setHasFixedSize(true);
    }

    private void populateUI(Movie movie) {

        mainBinding.titleTV.setText(movie.getOriginalTitle());
        mainBinding.releaseDateTV.setText(movie.getReleaseDate());
        String rating = getString(R.string.movie_rating, String.valueOf(movie.getRating()));
        mainBinding.ratingTV.setText(rating);
        mainBinding.synopsisTV.setText(movie.getSynopsis());

        Picasso.with(this)
                .load(Constants.API_IMAGE_BASE_URL + movie.getThumbnailUrl())
                .into(mainBinding.image);
    }

    private void loadTrailers() {
        if (NetworkUtils.isInternetAvailable(this)) {
            videoViewModel.init(movie.getId());
            videoViewModel.getVideos().observe(this, new Observer<List<Video>>() {
                @Override
                public void onChanged(@Nullable List<Video> videos) {
                    videoAdapter.setDataChanged(videos);
                }
            });
        }
    }

    private void loadReviews() {
        if (NetworkUtils.isInternetAvailable(this)) {
            reviewViewModel.init(movie.getId());
            reviewViewModel.getReviews().observe(this, new Observer<List<Review>>() {
                @Override
                public void onChanged(@Nullable List<Review> reviews) {
                    reviewAdapter.setDataChanged(reviews);
                }
            });
        }
    }

    private void saveMovie() {
        ExecutorUtils.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isMovieSaved) {
                    MovieRepository.getInstance(MovieDetailsActivity.this).delete(movie);
                    isMovieSaved = false;
                } else {
                    MovieRepository.getInstance(MovieDetailsActivity.this).save(movie);
                    isMovieSaved = true;
                }
                invalidateOptionsMenu();
            }
        });
    }

    private void showPersistenceState(Movie movie) {
        MovieDetailViewModelFactory factory = new
                MovieDetailViewModelFactory(MovieRepository.getInstance(this), movie.getId()) ;
        final MovieDetailViewModel  detailViewModel =
                ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);

        detailViewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                isMovieSaved = movie != null;
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void videoClicked(Video video) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW);
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        appIntent.setData(Uri.parse(YOUTUBE_APP + video.getKey()));
        appIntent.setData(Uri.parse(YOUTUBE_URL + video.getKey()));
        try {
            startActivity(appIntent);
        } catch (Exception e) {
            startActivity(webIntent);
        }
    }
}
