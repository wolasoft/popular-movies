package com.wolasoft.popularmovies.data.api.dto;

import com.google.gson.annotations.SerializedName;
import com.wolasoft.popularmovies.data.models.Review;

import java.util.List;

public class ReviewDTO {
    @SerializedName("results")
    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }
}
