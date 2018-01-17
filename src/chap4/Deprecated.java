package chap4;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Deprecated {


    public static void main(String[] args) throws InterruptedException {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Thread printThread = new Thread(new Runner(), "PrintThread");
        printThread.setDaemon(true);
        printThread.start();
        TimeUnit.SECONDS.sleep(3);

        //将PrintThread进行暂停，输出内容工作停止
        //Suspend调用后线程不会释放已经占有的资源（比如锁），而是占有着资源进入睡眠状态，容易
        //引发死锁
        printThread.suspend();
        System.out.println("main suspend PrintThread at " + dateFormat.format(new Date()));
        TimeUnit.SECONDS.sleep(3);
        //将PrintThread进行恢复，输出内容继续
        printThread.resume();
        System.out.println("main resume PringThread at " + dateFormat.format(new Date()));
        TimeUnit.SECONDS.sleep(3);
        //将printThread进行终止，输出内容停止
        //stop()方法在终结一个线程时不会保证线程的资源正常释放，通常是没有给予线程完成资源释放工作的机会
        //因此可能会导致程序可能工作在不确定状态下。
        printThread.stop();
        System.out.println("main stop PrintThread at " + dateFormat.format(new Date()));
        TimeUnit.SECONDS.sleep(3);
    }

    static class Runner implements Runnable{

        @Override
        public void run() {
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            while (true){
                System.out.println(Thread.currentThread().getName() + " Run at " + format.format(new Date()));
                SleepUtils.second(1);
            }
        }
    }

}
