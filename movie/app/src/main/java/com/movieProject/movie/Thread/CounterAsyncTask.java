package com.movieProject.movie.Thread;

import android.os.AsyncTask;
import android.widget.TextView;

public class CounterAsyncTask extends AsyncTask<Integer,Integer,String> {

    private TextView countUpTextView;

    public CounterAsyncTask(TextView textView) {
        this.countUpTextView = textView;
    }

    @Override
    protected void onPreExecute() {

    }
    @Override
    protected String doInBackground(Integer... numbers) {
        int i=0;
        if(numbers.length==1)
            i=numbers[0].intValue();
        for (int num=i;num<=10;num++) {
            if(!isCancelled()){
            publishProgress(num);//call to onProgressUpdate
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
            else{
            break;
            }
        }
        return "Done!";
    }

    @Override
    protected void onProgressUpdate(Integer ... value) {
        countUpTextView.setText(String.valueOf(value[0]));

    }

    @Override
    //get the return value from doInBackground method
    protected void onPostExecute(String s) {
        countUpTextView.setText(s);
    }

    @Override
    protected void onCancelled() {
        countUpTextView.setText("The Task Cancelled!");
    }
}
