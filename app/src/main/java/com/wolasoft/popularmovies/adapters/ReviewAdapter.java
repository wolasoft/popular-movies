package com.wolasoft.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolasoft.popularmovies.R;
import com.wolasoft.popularmovies.data.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> reviews;

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.review_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Review review = reviews.get(position);
        viewHolder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

    public void setDataChanged(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView authorTV;
        private final TextView contentTV;

        ViewHolder(View itemView) {
            super(itemView);
            authorTV = itemView.findViewById(R.id.authorTV);
            contentTV = itemView.findViewById(R.id.contentTV);
        }

        void bind(Review review) {
            authorTV.setText(review.getAuthor());
            contentTV.setText(review.getContent());
        }
    }
}
