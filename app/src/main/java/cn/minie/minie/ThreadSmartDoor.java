package cn.minie.minie;

/**
 * Created by cch on 16-3-11.
 */
public class ThreadSmartDoor extends SmartDoor {
    private final int testThreadCount = 4;
    public ThreadSmartDoor(SmartDoorCallbacks calllbacks) {
        super(calllbacks);
    }

    public void Start() {
        if (mReadThread == null) {
            mReadThread = new ReadThread(this);
            mReadThread.start();

//            mTestThreads = new FakeThread[testThreadCount];
//            for (int i = 0; i < testThreadCount; i++) {
//                mTestThreads[i] = new FakeThread();
//                mTestThreads[i].start();;
//            }
        }
    }
    public void Stop() {
        if (mReadThread != null)
            mReadThread.interrupt();

        mReadThread = null;

//        for (int i = 0; i < testThreadCount; i++) {
//            mTestThreads[i].interrupt();;
//            mTestThreads[i] = null;
//        }
//        mTestThreads = null;
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

//    private FakeThread[] mTestThreads;
//    private class FakeThread extends Thread {
//        @Override
//        public void run() {
//            super.run();
//            while (!isInterrupted()) {
//                int i = 10;
//                i = i + 1000;
//                double f = 10.021;
//                f = f * 100231.15818231;
//            }
//        }
//    }
}
