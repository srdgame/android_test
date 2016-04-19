package cn.minie.daemon;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

/**
 * Remote monitor service
 */
public class RemoteMonitor extends IntentService {
    private static final String TAG = "RemoteMonitor";

    private static final String ACTION_FETCH = "cn.minie.daemon.action.FETCH";
    private static final String ACTION_PING = "cn.minie.daemon.action.PING";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "cn.minie.daemon.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "cn.minie.daemon.extra.PARAM2";

    private Handler mHandler = new Handler();
    private Runnable mCheckUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                checkForUpdate();
                mHandler.postDelayed(mCheckUpdateRunnable, 5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public RemoteMonitor() {
        super("RemoteMonitor");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mThread.start();
        try {
            boolean r = mHandler.postDelayed(mCheckUpdateRunnable, 1000);
            if (!r) {
                Log.d(TAG, "Failed to pOstDelay!");
            } else {
                Log.d(TAG, "PostDellay!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkForUpdate() {
        Log.d(TAG, "Check for update....");
    }

    private void checkForUpdate2() {
        Log.d(TAG, "22 Check for update....");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy....");
        mHandler.removeCallbacks(mCheckUpdateRunnable);
        mThread.interrupt();
        super.onDestroy();
    }

    public static void startActionFetch(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RemoteMonitor.class);
        intent.setAction(ACTION_FETCH);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionPing(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RemoteMonitor.class);
        intent.setAction(ACTION_PING);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFetch(param1, param2);
            } else if (ACTION_PING.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionPing(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFetch(String param1, String param2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPing(String param1, String param2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private RunThread mThread = new RunThread();
    class RunThread extends Thread {
        @Override
        public void run() {
            try {
                while(!isInterrupted()) {
                    checkForUpdate2();
                    sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
