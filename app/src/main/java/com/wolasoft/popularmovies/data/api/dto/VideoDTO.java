package com.wolasoft.popularmovies.data.api.dto;

        import com.google.gson.annotations.SerializedName;
        import com.wolasoft.popularmovies.data.models.Video;

        import java.util.List;

public class VideoDTO {
    @SerializedName("results")
    private List<Video> videos;

    public List<Video> getVideos() {
        return videos;
    }
}
