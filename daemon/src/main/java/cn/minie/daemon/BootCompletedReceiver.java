package cn.minie.daemon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by cch on 16-4-19.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "Received boot completed event. Starting RemoteMonitor Service...");
            context.startService(new Intent(context, RemoteMonitor.class));
        } else {
            Log.d(TAG, "Received event " + intent.getAction());
        }
    }
}
