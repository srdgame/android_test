package cn.minie.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import cn.minie.aidl.IRemoteMonitorInterface;

public class RemoteMonitor extends Service {
    private static final String TAG = "RemoteMonitor";

    public RemoteMonitor() {
    }
    private Handler mHandler = new Handler();

    private IRemoteMonitorInterface.Stub mBinder = new IRemoteMonitorInterface.Stub() {

        @Override
        public boolean isOnline() throws RemoteException {
            return false;
        }
    };

    private Runnable mRunner = new Runnable() {
        @Override
        public void run() {
            checkUpdate();
            mHandler.postDelayed(mRunner, 5000);
        }
    };

    private void checkUpdate() {
        Log.d(TAG, "Check Update....");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate...");
        mHandler.postDelayed(mRunner, 1000);

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy....");
        mHandler.removeCallbacks(mRunner);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
