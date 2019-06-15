package com.movieProject.movie.SQLite;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.movieProject.movie.Models.VideoModel;

@Dao
public interface VideoDao {
    @Query("SELECT * FROM VideoModel WHERE movieId = :movieId")
    VideoModel getVideo(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VideoModel videoModel);
}
