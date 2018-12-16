package com.wolasoft.popularmovies;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wolasoft.popularmovies.models.Movie;
import com.wolasoft.popularmovies.utils.HttpUtils;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String SELECTED_MOVIE = "selected_movie";

    private TextView titleTv;
    private ImageView imageView;
    private TextView releaseDateTv;
    private TextView ratingTv;
    private TextView synopsisTv;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        imageView = findViewById(R.id.image);
        titleTv = findViewById(R.id.title_tv);
        releaseDateTv = findViewById(R.id.release_date_tv);
        ratingTv = findViewById(R.id.rating_tv);
        synopsisTv = findViewById(R.id.synopsis_tv);
        Intent intent = getIntent();

        if(savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(SELECTED_MOVIE);
        }

        if (movie != null) {
            populateUI(movie);
        }
        else if(intent.hasExtra(SELECTED_MOVIE)) {
            movie = intent.getParcelableExtra(SELECTED_MOVIE);
            populateUI(movie);
        }

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

    private void populateUI(Movie movie) {
        titleTv.setText(movie.getOriginalTitle());
        releaseDateTv.setText(movie.getReleaseDate());
        String rating = getString(R.string.movie_rating, String.valueOf(movie.getRating()));
        ratingTv.setText(rating);
        synopsisTv.setText(movie.getSynopsis());

        Picasso.with(this)
                .load(HttpUtils.API_IMAGE_BASE_URL + movie.getThumbnailUrl())
                .into(imageView);
    }
}
