package concurrency.threadpool;

import java.util.concurrent.LinkedBlockingQueue;

public class MyThreadPool {


    /**
     * first we need bloekedQueue and then we need thread schedule with each other
     */
    /**
     * 调度策略为轮训
     */
    Thread[] threads = null;
    private int coreSize;
    private int maxSize;

    // 阻塞队列
    // 为什么 LinkedBlockingQueue 要比 ArrayBlockingQueue 吞吐量高？
    LinkedBlockingQueue queue = null;

    public MyThreadPool() {
        this(2, Runtime.getRuntime().availableProcessors() * 10);
    }

    public MyThreadPool(int corePoolSize, int maxPoolSize) {
        this.coreSize = corePoolSize;
        this.maxSize = maxPoolSize;
        this.threads = new Thread[maxSize];
    }



    private void get() {

    }


}
