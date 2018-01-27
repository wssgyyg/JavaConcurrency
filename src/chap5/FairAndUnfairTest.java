package chap5;

import chap4.SleepUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairAndUnfairTest {

    private static Lock fairLock = new ReentrantLock2(true);
    private static Lock unfairLock = new ReentrantLock2(false);
    private static CountDownLatch countDownLatch = new CountDownLatch(1);


    private static class Job extends Thread {

        private Lock lock;

        public Job(Lock lock) {
            this.lock = lock;
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lock.lock();

            System.out.println("Lock by" + Thread.currentThread().getName());
            System.out.println("Waiting by" + ((ReentrantLock2) lock).getQueuedThreads());
            lock.unlock();

            lock.lock();
            System.out.println("Lock by" + Thread.currentThread().getName());
            System.out.println("Waiting by" + ((ReentrantLock2) lock).getQueuedThreads());


            lock.unlock();


        }
    }

    private static class ReentrantLock2 extends ReentrantLock {

        public ReentrantLock2(boolean fair) {
            super(fair);
        }

        public Collection<Thread> getQueuedThreads() {
            List<Thread> arrayList = new ArrayList<>(super.getQueuedThreads());
            Collections.reverse(arrayList);
            return arrayList;
        }

    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Job job = new Job(unfairLock);
            job.setDaemon(true);
            job.start();
        }
        countDownLatch.countDown();

        SleepUtils.second(10);

    }
}
