package cn.minie.minie;

import android.util.Log;

/**
 * Created by cch on 16-3-9.
 */
public class SmartDoor implements AutoCloseable {
    private final SmartDoorCallbacks callbacks;
    protected long jniObject; // Binding C pointer, *DO NOT* touch it in JAVA side.

    static {
        System.loadLibrary("minie");
    }

    public SmartDoor(SmartDoorCallbacks calllbacks) {
        callbacks = calllbacks;
        jniObject = 0;
    }

    /**
     * Override function from AutoCloseable, which make sure jniObject will be released.
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        destroy();
    }

    /**
     * Get JNI version
     * @return
     */
    public native String version();

    /**
     * Initialize the SmartDoor object
     * @param tty Serial port tty path. e.g. /dev/ttyS1
     * @param baudrate Serial port baudrate using for connecting with hardware. e.g. 115200
     * @param flags Serial port flags.  flags for calling open
     * @return result code
     */
    public native int init(String tty, int baudrate, int flags);

    /**
     * Destroy jni objects
     */
    public native void destroy();

    /**
     * Get all authed cards from hardwares
     * @return String in json format ( string array json)
     */
    public native String getCards();

    /**
     * Set authed cards by once, which will clear all previous authed cards
     * @param cards String in json format ( string array json)
     * @return result code
     */
    public native int setCards(String cards);

    /**
     * Clear all authed cards from hardware memory.
     * @return result code
     */
    public native int clearCards();

    /**
     * Add one authed card.
     * @param id Authed card identifier string
     * @return result code
     */
    public native int addCard(String id);

    /**
     * Remove one authed card.
     * @param id Authed card identifier string
     * @return result code
     */
    public native int removeCard(String id);

    /**
     * Running self test.
     * @return result String.
     */
    public native String tests();

    /**
     * Trigger jni running loop.
     */
    public native void Run();

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Following are callback wrapper functions.
    ///////////////////////////////////////////////////////////////////////////////////////////////
    protected void onInit(int code) {
        callbacks.onInit(code);
    }
    protected void onClose() {
        callbacks.onClose();
    }
    protected void onPunch(String card, int err) {
        callbacks.onPunch(card, err);
    }
    protected boolean onCode(int code) {
        return callbacks.onCode(code);
    }
    protected boolean checkCard(String card) {
        return callbacks.checkCard(card);
    }
    protected String onTest(String test) {
        return test + "onTest";
    }
}
