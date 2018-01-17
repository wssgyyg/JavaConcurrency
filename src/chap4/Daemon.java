package chap4;

/**
 * Deamon线程是一种支持型线程，主要被用作程序中后台调度以及支持性工作。
 * 当一个JVM中不存在非Daemon线程的时候，JVM将会退出。
 */
public class Daemon {

    /**
     * Main 方法结束后虚拟机中已经没有非Daemon线程，JVM要退出，所有的Daemon线程都需要立即终止，因此
     * DaemonRunner立即终止，但是finally块没有执行。
     *
     * 构建Daemon线程时，不能依靠finally块中的内容来确保执行关闭或清理资源的逻辑。
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new DaemonRunner(), "DaemonRunner");
        thread.setDaemon(true);
        thread.start();

        Thread.sleep(5000);

    }

    static class DaemonRunner implements Runnable {

        @Override
        public void run() {
            try {
                SleepUtils.second(1);
            } finally {
                System.out.println("DaemonThread finally run.");
            }
        }
    }
}
