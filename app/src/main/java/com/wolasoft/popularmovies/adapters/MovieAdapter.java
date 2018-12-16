package com.wolasoft.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wolasoft.popularmovies.R;
import com.wolasoft.popularmovies.models.Movie;
import com.wolasoft.popularmovies.utils.HttpUtils;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movieList;
    private OnMovieClickedListener listener;

    public MovieAdapter(List<Movie> movieList, OnMovieClickedListener listener ) {
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movies_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Movie movie = movieList.get(position);
        viewHolder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public void setDataChanged(List<Movie> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context context;
        private ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        void bind(Movie movie) {
            Picasso.with(context)
                    .load(HttpUtils.API_IMAGE_BASE_URL + movie.getThumbnailUrl())
                    .into(imageView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movie = movieList.get(position);
            listener.movieClicked(movie);
        }
    }

    public interface OnMovieClickedListener {
        void movieClicked(Movie movie);
    }
}
