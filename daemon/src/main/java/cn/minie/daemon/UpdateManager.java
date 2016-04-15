package cn.minie.daemon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by cch on 16-4-14.
*/
public class UpdateManager {
    private static final int DOWNLOAD = 1;
    private static final int DOWNLOAD_FINISH = 2;
    private static final int DOWNLOAD_FINALLY = 3;
    private static final int DOWNLOAD_TIMEOUT = 3600 * 1000;
    private static final int INSTALL_APK_TIMEOUT = 2 * 60 * 1000;
    private final Context mContext;
    private boolean cancelUpdate = false;
    private HashMap<String, Date> mDownloadMap = new HashMap<String, Date>();

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    //mProgress.setProgress(msg.arg1);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk((String)msg.obj);
                    break;
                case DOWNLOAD_FINALLY:
                    downloadFinally((String)msg.obj);
                    break;
                default:
                    break;
            }
        };
    };
    public UpdateManager(Context context) {
        mContext = context;
    }
    public int DownloadApk(String apkUrl, String apkName) {
        Date now = new Date();
        if (mDownloadMap.containsKey(apkUrl)) {
            if (now.getTime() < mDownloadMap.get(apkUrl).getTime()) {
                return -999;
            }
        }
        now.setTime(now.getTime() + DOWNLOAD_TIMEOUT);
        mDownloadMap.put(apkUrl, now);
        new downloadApkThread(apkUrl, apkName).start();
        return 0;
    }
    private class downloadApkThread extends Thread
    {
        private final String mApkName;
        private final String mApkUrl;
        private final String mSavePath;

        public downloadApkThread(String apkUrl, String apkName) {
            mApkUrl = apkUrl;
            mApkName = apkName;
            //mSavePath = Environment.getExternalStorageDirectory() + "/Download";
            mSavePath = "/sdcard/Download";
        }
        @Override
        public void run()
        {
            try
            {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    // 获得存储卡的路径
                    URL url = new URL(mApkUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists())
                    {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, mApkName);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        int progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD, progress, 0));
                        //mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0)
                        {
                            // 下载完成
                            mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_FINISH, apkFile.toString()));
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (Exception e)
            {
                e.printStackTrace();;
            } finally {
                mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_FINALLY, mApkUrl));
            }
        }
    };

    private void installApk(String apkPath)
    {
        InstallApk.installAndStartApk(mContext, apkPath);
    }

    private void downloadFinally(String apkUrl)
    {
        Date now = new Date();
        now.setTime(now.getTime() + INSTALL_APK_TIMEOUT);
        mDownloadMap.put(apkUrl, now);
    }
}

