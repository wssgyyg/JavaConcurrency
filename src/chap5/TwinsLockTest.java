package chap5;

import chap4.SleepUtils;

import java.util.concurrent.locks.Lock;

public class TwinsLockTest {

    static final Lock lock = new TwinsLock();

    static class Worker extends Thread {

        public Worker(String threadName) {
            super(threadName);
        }

        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    SleepUtils.second(1);
                    System.out.println(Thread.currentThread().getName());
                    SleepUtils.second(1);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    //启动10个线程
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker("thread-" + i);
            worker.setDaemon(true);
            worker.start();
        }

        for (int i = 0; i < 10; i++) {
            SleepUtils.second(1);
            System.out.println();
        }

        SleepUtils.second(10);
    }



}
