package com.movieProject.movie.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

public class BGIntentService extends IntentService {
    private boolean isDestroyed;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BGIntentService(String name) {
        super(name);
    }

    public BGIntentService(){
        super("BGIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        isDestroyed = false;
        for (int i = 0; i <= 100 && !isDestroyed; i++) {
            SystemClock.sleep(100);
            Intent broadcastIntent = new Intent(BGServiceActivity.BackgroundProgressReceiver.PROGRESS_UPDATE_ACTION);
            broadcastIntent.putExtra(BGServiceActivity.BackgroundProgressReceiver.PROGRESS_VALUE_KEY, i);
            sendBroadcast(broadcastIntent);
        }
    }
}
