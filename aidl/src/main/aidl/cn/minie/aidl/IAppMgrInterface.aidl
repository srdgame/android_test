// IAppMgrInterface.aidl
package cn.minie.aidl;

// Declare any non-default types here with import statements

interface IAppMgrInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    int InstallRemote(String apkUrl);
    int Install(String apkPath);
    int Start(String packageName, String activityName);
}
