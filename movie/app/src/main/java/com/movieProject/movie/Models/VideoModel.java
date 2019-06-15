package com.movieProject.movie.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class VideoModel {
    @PrimaryKey
    private int movieId;
    private String id;
    private String key;

    public VideoModel(){};

    public VideoModel(int id, String videoId, String videoKey) {
        this.movieId=id;
        this.id=videoId;
        this.key=videoKey;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
