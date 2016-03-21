package cn.minie.minie;

import android.util.Log;

/**
 * Created by cch on 16-3-9.
 */
public class SmartDoor {
    private final SmartDoorCallbacks callbacks;
    private static final String TAG = "SmartDoor";

    static {
        System.loadLibrary("minie");
    }

    public SmartDoor(SmartDoorCallbacks calllbacks) {
        callbacks = calllbacks;
    }

    public native String version();
    public native int init(String tty, int baudrate, int flags);
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
    protected void onPunch(String card, int err) {
        Log.d(TAG, "onPunch: Test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        callbacks.onPunch(card, err);
    }
    protected boolean onCode(int code) {
        Log.d(TAG, "onCode: Test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return callbacks.onCode(code);
    }
    protected boolean checkCard(String card) {
        Log.d(TAG, "checkCard: Called with card" + card);
        return callbacks.checkCard(card);
    }
    protected String onTest(String test) {
        return test + "onTest";
    }

}
