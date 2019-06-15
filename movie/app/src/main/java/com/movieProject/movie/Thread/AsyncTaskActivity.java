package com.movieProject.movie.Thread;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.movieProject.movie.R;

public class AsyncTaskActivity extends AppCompatActivity  {

    private AsyncTaskFragments asyncTaskFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asynctask_activity);
        if(asyncTaskFragments==null &&savedInstanceState==null){
            asyncTaskFragments=new AsyncTaskFragments();
            getSupportFragmentManager().beginTransaction().add(R.id.myFrame,asyncTaskFragments).commit();
        }
        else if(asyncTaskFragments==null){
            asyncTaskFragments=(AsyncTaskFragments) getSupportFragmentManager().findFragmentById(R.id.myFrame);
        }
    }
}
