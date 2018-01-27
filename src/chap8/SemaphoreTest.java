package chap8;

import chap4.SleepUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 信号量
 * 控制同时访问特定资源的线程数量
 */
public class SemaphoreTest {

    private static final int THREAD_COUNT = 30;

    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

    private static Semaphore s = new Semaphore(15);

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPool.execute(
                    () -> {
                        try {
                            s.acquire();
                            System.out.println("save data");
                            SleepUtils.second(5);
                            s.release();
                        } catch (InterruptedException e) {

                        }
                    }
            );
        }

        threadPool.shutdown();
    }

}
