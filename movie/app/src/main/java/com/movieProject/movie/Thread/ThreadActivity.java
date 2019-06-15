package com.movieProject.movie.Thread;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.movieProject.movie.R;

public class ThreadActivity extends AppCompatActivity {
    private ThreadFragments threadFragments;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asynctask_activity);
        if(threadFragments==null){
            threadFragments=new ThreadFragments();
            getSupportFragmentManager().beginTransaction().add(R.id.myFrame,threadFragments).commit();
        }
    }


}
