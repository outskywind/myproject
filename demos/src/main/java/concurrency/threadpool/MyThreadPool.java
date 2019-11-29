package concurrency.threadpool;

import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyThreadPool {

    private static Logger logger = LoggerFactory.getLogger(MyThreadPool.class);

    /**
     * first we need bloekedQueue and then we need thread schedule with each other
     */
    /**
     * 这个是用来判断是否需要新增线程，以及结束多余线程的条件
     * 线程池的主要是实现阻塞队列，消费者会在takeLock锁上等待锁
     * 直到获取到元素。
     */
    /**
     * 空闲线程
     */
    LinkedList<Thread> available = new LinkedList<Thread>();
    /**
     * 运行中线程
     */
    LinkedList<Thread> running = new LinkedList<Thread>();

    private int coreSize;

    private int maxSize;
    // 线程总数
    private volatile AtomicInteger count = new AtomicInteger(0);

    // 阻塞队列
    // 为什么 LinkedBlockingQueue 要比 ArrayBlockingQueue 吞吐量高？
    // 因为ArrayBlockingQueue 实现读写都是一个锁，而LinkedBlockingQueue 读与写的锁分开。
    private LinkedBlockingQueue<Runnable> linkedQueue = new LinkedBlockingQueue<Runnable>();

    private ReentrantLock lock = new ReentrantLock();
    // private Condition notEmpty = lock.newCondition();

    ArrayBlockingQueue<Runnable> arrayQueue = null;



    public MyThreadPool() {
        // t_cpu+t_io = Nthread*t_cpu
        // Ncpu核数，考虑的是此时同时终止，那么就需要Nthread*t_cpu*Ncpu
        this(2, Runtime.getRuntime().availableProcessors() * 10);
        ExecutorService service = Executors.newFixedThreadPool(10);
    }

    public MyThreadPool(int corePoolSize, int maxPoolSize) {
        this.coreSize = corePoolSize;
        this.maxSize = maxPoolSize;
    }


    /**
     * 实现消费者逻辑
     * 
     * @author leo.quan
     *
     */
    public static class ScheduleRun implements Runnable {
        private LinkedBlockingQueue<Runnable> linkedQueue = null;

        public ScheduleRun(LinkedBlockingQueue<Runnable> linkedQueue) {
            this.linkedQueue = linkedQueue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // 阻塞
                    Runnable task = this.linkedQueue.poll();
                    task.run();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 执行完成 睡眠，或者直接进入下一轮阻塞等待
            }
        }
        }

    /**
     * 
     * @param run
     */
    public void submit(Runnable run) {
        try {
            Thread t = adjustThread();
            if (t == null) {
                linkedQueue.put(run);
            }
        } catch (Exception e) {
            logger.error("submit exception :", e);
        }
    }

    /**
     * 1.获取available 状态线程
     * 2.如果有进入running 返回，无判断count < maxSize
     * 3.如果小于，则新增一条线程，进入running.
     * 4.如果否，直接返回。runnable 进入阻塞队列。
     * @return
     */
    private Thread adjustThread() {
        Thread t = available.poll();
        if (t != null) {
            running.offer(t);
            return t;
        }
        if (count.getAndIncrement() < maxSize) {
            t = new Thread(new ScheduleRun(linkedQueue));
            t.start();
            running.offer(t);
            return t;
        }
        return null;
    }


}
