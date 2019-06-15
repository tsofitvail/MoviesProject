package com.movieProject.movie.Thread;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.TextView;

public class MySimpleAsyncTask {

    private TextView countUpTextView;
    private boolean canceled;
    private Thread BackgroundThread;
    private String doneText;


    public MySimpleAsyncTask(TextView countUpTextView) {
        this.countUpTextView = countUpTextView;
        this.canceled = false;
        this.doneText="";
    }

    protected  void doInBackground(){
        int end = 10;
        for (int i = 1; i <= end; i++) {
            if(canceled) {
                return;
            }
            publishProgress(i);
            SystemClock.sleep(2000);
        }
        doneText="Done!";
    }


    protected void onPreExecute() { }

    public void execute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onPreExecute();
                BackgroundThread = new Thread("Handler_executor_thread") {
                    @Override
                    public void run() {
                        doInBackground();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onPostExecute();
                            }
                        });
                    }
                };
                BackgroundThread.start();
            }
        });
    }


    private void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }


    protected void publishProgress(final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onProgressUpdate(progress);
            }
        });
    }

    //get the return value from doInBackground method
    protected void onPostExecute() {
        countUpTextView.setText(doneText);
    }

    protected void onCancelled() {
        countUpTextView.setText("The Task Cancelled!");
        canceled=true;
        BackgroundThread.interrupt();

    }

    private void onProgressUpdate(int progress) {
            countUpTextView.setText(String.valueOf(progress));

    }

}

