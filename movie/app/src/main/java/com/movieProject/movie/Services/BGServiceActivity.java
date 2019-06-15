package com.movieProject.movie.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.movieProject.movie.R;

import static com.movieProject.movie.Services.BGServiceActivity.BackgroundProgressReceiver.PROGRESS_UPDATE_ACTION;

public class BGServiceActivity extends AppCompatActivity implements View.OnClickListener {
    private Button intentServiceButton,serviceButton;
    private TextView progressTextView;
    private BackgroundProgressReceiver BGprocessReciver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        intentServiceButton=findViewById(R.id.intentServiceButton);
        serviceButton=findViewById(R.id.serviceButton);
        intentServiceButton.setOnClickListener(this);
        serviceButton.setOnClickListener(this);
        progressTextView=findViewById(R.id.progressInPercentage);
    }

    @Override
    protected void onPause() {
        if(BGprocessReciver!=null)
            unregisterReceiver(BGprocessReciver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        subscribeForProgressUpdates();
        super.onResume();
    }

    private void subscribeForProgressUpdates() {
        if (BGprocessReciver == null) {
            BGprocessReciver = new BackgroundProgressReceiver();
        }
        IntentFilter progressUpdateActionFilter = new IntentFilter(PROGRESS_UPDATE_ACTION);
        registerReceiver(BGprocessReciver, progressUpdateActionFilter);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.intentServiceButton:
                Thread intentThread=new Thread(){
                    @Override
                    public void run() {
                        Intent intent=new Intent(getApplicationContext(),BGIntentService.class);
                          startService(intent);
                    }
                };
                intentThread.start();
                break;
            case R.id.serviceButton:
                intent=new Intent(v.getContext(),BGService.class);
                startService(intent);
                break;

        }

    }

    public class BackgroundProgressReceiver extends BroadcastReceiver {
        public static final String PROGRESS_UPDATE_ACTION = "PROGRESS_UPDATE_ACTION";
        public static final String PROGRESS_VALUE_KEY = "PROGRESS_VALUE_KEY";
        public static final String SERVICE_STATUS = "SERVICE_STATUS";
        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra(PROGRESS_VALUE_KEY, -1);
            if(progress==100)
                progressTextView.setText("Done!");
            else {
                String presentage=String.valueOf(progress)+'%';
                progressTextView.setText(presentage);
            }

        }
    }
}
