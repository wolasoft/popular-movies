package com.wolasoft.popularmovies.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movies")
public class Movie implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String originalTitle;

    @SerializedName("poster_path")
    private String thumbnailUrl;

    @SerializedName("vote_average")
    private float rating;

    @SerializedName("overview")
    private String synopsis;

    @SerializedName("release_date")
    @ColumnInfo(name = "overview")
    private String releaseDate;

    @Ignore
    public Movie() {}

    public Movie(int id, String originalTitle, String thumbnailUrl, float rating, String synopsis, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.thumbnailUrl = thumbnailUrl;
        this.rating = rating;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel parcel) {
        this.id = parcel.readInt();
        this.originalTitle = parcel.readString();
        this.thumbnailUrl = parcel.readString();
        this.rating = parcel.readFloat();
        this.synopsis = parcel.readString();
        this.releaseDate = parcel.readString();
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public float getRating() {
        return rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(thumbnailUrl);
        dest.writeFloat(rating);
        dest.writeString(synopsis);
        dest.writeString(releaseDate);
    }

    static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };
}
