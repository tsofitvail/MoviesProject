package com.movieProject.movie.SQLite;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.movieProject.movie.Models.MovieModel;

import java.util.Collection;
import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM MovieModel ORDER BY popularity DESC")
    List<MovieModel> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Collection<MovieModel> movies);

    @Query("DELETE FROM MovieModel")
    void deleteAll();

}
