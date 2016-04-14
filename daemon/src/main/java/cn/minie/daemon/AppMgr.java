package cn.minie.daemon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import cn.minie.aidl.IAppMgrInterface;

public class AppMgr extends Service {
    public AppMgr() {
    }

    private Handler mHandler = new Handler();

    private IAppMgrInterface.Stub mBinder = new IAppMgrInterface.Stub() {
        @Override
        public int Install(final String apkPath) throws RemoteException {
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    InstallApk.installAndStartApk(getApplicationContext(), apkPath);
//                }
//            });
            return InstallApk.installAndStartApk(getApplicationContext(), apkPath);
        }

        @Override
        public int Start(final String packageName, final String activityName) throws RemoteException {
            InstallApk.startApk(packageName, activityName);
            return 0;
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
