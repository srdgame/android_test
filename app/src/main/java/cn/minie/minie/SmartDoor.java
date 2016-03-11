package cn.minie.minie;

import android.util.Log;

/**
 * Created by cch on 16-3-9.
 */
public class SmartDoor {
    private final SmartDoorCallbacks callbacks;

    public SmartDoor(SmartDoorCallbacks calllbacks) {
        callbacks = calllbacks;
    }
    private static final String TAG = "SmartDoor";

    static {
        System.loadLibrary("minie");
    }

    public native String version();
    public native int init();
    public native String getCards();
    public native int setCards(String cards);
    public native int addCard(String id);
    public native int removeCard(String id);

    public native String tests();
    public native void Run();

    protected void onInit(int code) {
        Log.d(TAG, "onInit: Test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        callbacks.onInit(code);
    }
    protected void onClose() {
        Log.d(TAG, "onClose: Test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        callbacks.onClose();
    }
    protected void onPunch(String card) {
        Log.d(TAG, "onPunch: Test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        callbacks.onPunch(card);
    }
    protected boolean onCode(String code) {
        Log.d(TAG, "onCode: Test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return callbacks.onCode(code);
    }
    protected boolean checkCard(String card) {
        Log.d(TAG, "checkCard: Called with card" + card);
        return callbacks.onCode(card);
    }
    protected String onTest(String test) {
        return test + "onTest";
    }

}
