package com.wolasoft.popularmovies.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.wolasoft.popularmovies.data.models.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAll();
    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<Movie> getById(int id);
    @Delete()
    void delete(Movie movie);
}
