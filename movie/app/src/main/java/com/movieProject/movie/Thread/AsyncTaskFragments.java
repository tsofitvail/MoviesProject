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

public class AsyncTaskFragments extends Fragment implements View.OnClickListener{

    private TextView counterUp;
    private Button createButton, startButton, cancelButton;
    private CounterAsyncTask countTask;

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
        if(savedInstanceState==null){
        counterUp.setText("This is a AsyncTask Activity");
        }
        else {
                countTask = new CounterAsyncTask(counterUp);
                String startNumber = savedInstanceState.getString("COUNTER_UP").toString();
                counterUp.setText(startNumber);
                countTask.execute(Integer.parseInt(startNumber));
            }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create:
                countTask=new CounterAsyncTask(counterUp);
                Toast.makeText(getContext(),"Create AsyncTask",Toast.LENGTH_SHORT).show();
                break;
            case R.id.start:
                Toast.makeText(getContext(),"AsyncTask Started",Toast.LENGTH_SHORT).show();
                countTask.execute(1,2,3,4,5,6,7,8,9,10);
                break;
            case R.id.cancel:
                Toast.makeText(getContext(),"AsyncTask Canceled",Toast.LENGTH_SHORT).show();
                countTask.cancel(true);
        }
    }

    @Override
    public void onStop() {
        if(countTask!=null){
            if(!countTask.isCancelled()) {
            countTask.cancel(true);
         }
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if(countTask!=null){
            if(!countTask.isCancelled()) {
                countTask.cancel(true);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if(counterUp!=null) {
            bundle.putString("COUNTER_UP", counterUp.getText().toString());
        }

    }


}
