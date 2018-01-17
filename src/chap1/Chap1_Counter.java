package chap1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Chap1_Counter {

    private AtomicInteger atomicI = new AtomicInteger(0);

    private int i = 0;

    public static void main(String[] args) {

        final Chap1_Counter cas = new Chap1_Counter();
        List<Thread> ts = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int j = 0; j < 100; j++){
            Thread t = new Thread(() -> {
                for (int i = 0; i < 10000; i++){
                    cas.count();
                    cas.safeCount();
                }
            });
            ts.add(t);
        }

        ts.stream().forEach(
                thread -> thread.start()
        );

        for (Thread thread : ts){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(cas.i);
        System.out.println(cas.atomicI.get());
        System.out.println(System.currentTimeMillis() - start);

    }

    /**
     * 使用Compare And Swap实现线程安全计数器
     */
    private void safeCount(){
        for (;;){
            int i = atomicI.get();
            boolean suc = atomicI.compareAndSet(i, ++i);
            if (suc){
                break;
            }
        }
    }

    /**
     * 非线程安全计数器
     */
    public void count(){
        i++;
    }

}
