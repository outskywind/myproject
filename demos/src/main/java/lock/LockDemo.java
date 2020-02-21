package lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class LockDemo {

    static Logger logger = Logger.getLogger(LockDemo.class.getName());





    public static void main(String[] args){

        Thread t1 = new Thread(() -> {
            logger.info("t1 start");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("t1 end");
        });
        Thread t2 = new Thread(() -> {
            logger.info("t2 start");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("t2 end");
        });
        t1.start();
        try {
            t1.join();//wait util this  thread  die terminate join 是 同步的方法，因此获取了Thread 实例自身的monitor lock
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();

        //
        Lock lock = new ReentrantLock();
        lock.lock();
        Condition condition1 = lock.newCondition();
        try {
            condition1.await();//在这个条件上阻塞并且释放锁
            condition1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }








}
