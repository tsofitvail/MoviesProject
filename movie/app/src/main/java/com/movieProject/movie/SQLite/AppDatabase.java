package com.movieProject.movie.SQLite;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.movieProject.movie.Models.MovieModel;
import com.movieProject.movie.Models.VideoModel;

import java.util.List;

@Database(entities = {MovieModel.class,VideoModel.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "movies.db";
    private static AppDatabase INSTANCE;

    public abstract MovieDao movieDao();
    public abstract VideoDao videoDao();


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()//tells Room it is allowed to delete the old data
                    .allowMainThreadQueries()//Disables the main thread query check for Room
                    .build();
        }
        return INSTANCE;
    }

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
