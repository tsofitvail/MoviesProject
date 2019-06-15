package com.movieProject.movie.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import static android.content.ContentValues.TAG;
import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class BGService extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private boolean isDestroyed;
    @Override
    public void onCreate() {
        // To avoid cpu-blocking, we create a background handler to run our service
        HandlerThread thread = new HandlerThread(TAG, THREAD_PRIORITY_BACKGROUND);
        // start the new handler thread
        thread.start();
        mServiceLooper = thread.getLooper();
        // start the service using the background handler
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        isDestroyed = false;
        // call a new service handler. The service ID can be used to identify the service
        Message message = mServiceHandler.obtainMessage();
        message.arg1 = startId;
        mServiceHandler.sendMessage(message);

        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
                super(looper);
        }

        @Override
        // Well calling mServiceHandler.sendMessage(message);  from onStartCommand this method will be called.
        public void handleMessage(Message msg) {
            // Add your cpu-blocking activity here
            for (int i = 0; i <= 100 && !isDestroyed; i++) {
                SystemClock.sleep(100);
                Intent intent = new Intent(BGServiceActivity.BackgroundProgressReceiver.PROGRESS_UPDATE_ACTION);
                intent.putExtra(BGServiceActivity.BackgroundProgressReceiver.PROGRESS_VALUE_KEY, i);
                sendBroadcast(intent);
            }
            // the msg.arg1 is the startId used in the onStartCommand,so we can track the running service here.
            stopSelf(msg.arg1);
        }
    }


}
