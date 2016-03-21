package cn.minie.minie;

/**
 * Created by cch on 16-3-11.
 */
public class ThreadSmartDoor extends SmartDoor {

    public ThreadSmartDoor(SmartDoorCallbacks calllbacks) {
        super(calllbacks);
    }

    public void Start() {
        if (mReadThread == null) {
            mReadThread = new ReadThread(this);
            mReadThread.start();
        }
    }
    public void Stop() {
        if (mReadThread != null)
            mReadThread.interrupt();
        mReadThread = null;
    }

    private ReadThread mReadThread;
    private class ReadThread extends Thread {
        private final ThreadSmartDoor mdoor;
        ReadThread(ThreadSmartDoor door) {
            mdoor = door;
        }
        @Override
        public void run() {
            super.run();
            while(!isInterrupted()) {
                int size;
                try {
                    mdoor.Run();
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

}
