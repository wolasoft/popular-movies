package com.wolasoft.popularmovies.data.api.services;

import com.wolasoft.popularmovies.data.api.dto.MovieDTO;
import com.wolasoft.popularmovies.data.api.dto.ReviewDTO;
import com.wolasoft.popularmovies.data.api.dto.VideoDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieService {
    @GET("movie/{path}")
    Call<MovieDTO> getAll(@Path("path") String path);

    @GET("movie/{id}/reviews")
    Call<ReviewDTO> getReviews(@Path("id") int id);

    @GET("movie/{id}/videos")
    Call<VideoDTO> getVideos(@Path("id") int id);
}
