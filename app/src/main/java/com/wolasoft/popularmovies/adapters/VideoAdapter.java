package com.wolasoft.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolasoft.popularmovies.R;
import com.wolasoft.popularmovies.data.models.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<Video> videos;
    private final OnVideoClickedListener listener;

    public VideoAdapter(List<Video> videos, OnVideoClickedListener listener ) {
        this.videos = videos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.video_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Video video = videos.get(position);
        viewHolder.bind(video);
    }

    @Override
    public int getItemCount() {
        return videos == null ? 0 : videos.size();
    }

    public void setDataChanged(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nameTV;

        ViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            itemView.setOnClickListener(this);
        }

        void bind(Video video) {
            nameTV.setText(video.getName());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Video video = videos.get(position);
            listener.videoClicked(video);
        }
    }

    public interface OnVideoClickedListener {
        void videoClicked(Video video);
    }
}
