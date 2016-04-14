package cn.minie.daemon;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class InstallApk {

    private static final String TAG = "InstallApk";

    public static int installAndStartApk(final Context context, final String apkPath) {
        if ((apkPath==null) || (context==null)) {
            return -1;
        }

        File file = new File(apkPath);
        if (file.exists() == false) {
            return -2;
        }

        new Thread() {
            public void run() {
                String packageName = getUninstallApkPackageName(context, apkPath);
                //if (silentInstall(apkPath)) {
                if (installSlient(apkPath) == 0) {
                    List<ResolveInfo> matches = findActivitiesForPackage(context, packageName);
                    if ((matches!=null) && (matches.size()>0)) {
                        ResolveInfo resolveInfo = matches.get(0);
                        ActivityInfo activityInfo = resolveInfo.activityInfo;
                        //startApk(activityInfo.packageName, activityInfo.name);
                        apkStart(activityInfo.packageName, activityInfo.name);
                    }
                }
            };
        }.start();
        return 0;
    }

    public static String getUninstallApkPackageName(Context context, String apkPath) {
        String packageName = null;
        if (apkPath == null) {
            return packageName;
        }

        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info == null) {
            return packageName;
        }

        packageName = info.packageName;
        return packageName;
    }

    public static List<ResolveInfo> findActivitiesForPackage(Context context, String packageName) {
        final PackageManager pm = context.getPackageManager();

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setPackage(packageName);

        final List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent, 0);
        return apps != null ? apps : new ArrayList<ResolveInfo>();
    }

    public static boolean silentInstall(String apkPath) {
        String cmd1 = "chmod 777 " + apkPath + " \n";
        String cmd2 = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r " + apkPath + " \n";
        return execWithSID(cmd1, cmd2);
    }

    private static boolean execWithSID(String... args) {
        boolean isSuccess = false;
        Process process = null;
        OutputStream out = null;
        try {
            process = Runtime.getRuntime().exec("su");
            out = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(out);

            for (String tmp : args) {
                dataOutputStream.writeBytes(tmp);
            }

            dataOutputStream.flush(); // 提交命令
            dataOutputStream.close(); // 关闭流操作
            out.close();

            isSuccess = waitForProcess(process);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isSuccess;
    }

    public static boolean startApk(String packageName, String activityName) {
        boolean isSuccess = false;

        String cmd = "am start --user 0 -n " + packageName + "/" + activityName + " \n";
        try {
            Process process = Runtime.getRuntime().exec(cmd);

            isSuccess = waitForProcess(process);
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
            e.printStackTrace();
        }
        return isSuccess;
    }

    private static boolean waitForProcess(Process p) {
        boolean isSuccess = false;
        int returnCode;
        try {
            returnCode = p.waitFor();
            switch (returnCode) {
                case 0:
                    isSuccess = true;
                    break;

                case 1:
                    break;

                default:
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return isSuccess;
    }



    /**
     * install slient
     *
     * @param filePath
     * @return 0 means normal, 1 means file not exist, 2 means other exception error
     */
    public static int installSlient(String filePath) {
        String[] args = { "pm", "install", "-r", filePath };
        ProcessBuilder processBuilder = new ProcessBuilder(args);

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        int result;
        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;

            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }

            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = -1;
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }

        // TODO should add memory is not enough here
        if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
            result = 0;
        } else {
            result = -2;
        }
        Log.d(TAG, "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg);
        return result;
    }

    public static int apkStart(String packageName, String activityName) {
        String[] args = { "am", "start", "--user", "0", "-n", packageName + "/" + activityName };
        ProcessBuilder processBuilder = new ProcessBuilder(args);

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        int result;
        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;

            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }

            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = -1;
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }

        // TODO should add memory is not enough here
        if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
            result = 0;
        } else {
            result = -2;
        }
        Log.d(TAG, "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg);
        return result;
    }
}