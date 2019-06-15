package com.movieProject.movie.Thread;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.movieProject.movie.R;

public class ThreadFragments extends Fragment implements View.OnClickListener{

    private TextView counterUp;
    private Button createButton, startButton, cancelButton;
    private MySimpleAsyncTask mySimpleAsyncTask;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.counter_fragment,container,false);
        createButton=view.findViewById(R.id.create);
        startButton=view.findViewById(R.id.start);
        cancelButton=view.findViewById(R.id.cancel);
        createButton.setOnClickListener(this);
        startButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        counterUp=view.findViewById(R.id.counter);
        counterUp.setText("This is a Thread Activity");
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create:
                mySimpleAsyncTask=new MySimpleAsyncTask(counterUp);
                Toast.makeText(getContext(),"Create AsyncTask",Toast.LENGTH_SHORT).show();
                break;
            case R.id.start:
                Toast.makeText(getContext(),"AsyncTask Started",Toast.LENGTH_SHORT).show();
                mySimpleAsyncTask.execute();
                break;
            case R.id.cancel:
                Toast.makeText(getContext(),"AsyncTask Canceled",Toast.LENGTH_SHORT).show();
                mySimpleAsyncTask.onCancelled();
                //  countTask.cancel(true);
        }
    }
}
