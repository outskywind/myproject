package concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSemaphoreTest {

    private static int DEFAULT_THRESHOLD =
            Integer.parseInt(System.getProperty("com.concurrency.ThreadSemaphoreTest.defaultThreshold", "2"));
    private static Semaphore sem = new Semaphore(DEFAULT_THRESHOLD);

    private static AtomicInteger count = new AtomicInteger(0);

    private static long countDownStamp;


    /**
     * Semaphore 用来限制代码块资源的并发访问数量
     */
    /**
     * 如果这个线程的run里面抛出了未捕获的异常，这个线程会死掉
     * 
     * @author leo.quan
     *
     */
    public static class ThreadImplTask implements Runnable {

        private CountDownLatch latch = null;

        public ThreadImplTask(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            int round = 0;
            try {
                latch.await();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            while (true) {

                // call semaphore method
                runTest();
                if (round++ == 3) {
                    System.out.println("thread " + Thread.currentThread().getId() + " terminates");
                    break;
                }
            }
        }

    }


    public static void main(String[] args) throws InterruptedException {
        int count = 4;
        Thread[] ts = new Thread[count];
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            Thread ti = new Thread(new ThreadImplTask(latch));
            ti.start();
            ts[i] = ti;
            latch.countDown();
            countDownStamp = System.currentTimeMillis();
        }
        long start = System.currentTimeMillis();
        for (Thread tss : ts) {
            // 主线程等待每个线程10s结束,means 结束，如果子线程循环不结束呢？子线程一直存在
            // 如果10s没结束，也会继续运行，不再等待
            // waiting on conditon 条件等待
            tss.join(1000);
            // 然后永远等待直到此子线程结束
            System.out.println("main thread waits after join 1s");
            tss.join();
        }
        Thread neverEnd = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // we never terninate
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        neverEnd.start();
        // we main thread waits for nerverEnd thread
        System.out.println("we start waitting on nerver end thread  join ...");
        neverEnd.join();

        long cur = System.currentTimeMillis();
        System.out.println("join on  threads for total " + ((cur - start) / 1000) + "seconds");
        System.out.println("main thread terminate,total count = " + ThreadSemaphoreTest.count.get());

    }


    public static void runTest() {
        // 这里一般不这么用，因为随时会有释放semaphore 的线程，所以会变化的
        if (sem.availablePermits() == 0) {
            System.out.println("[" + (System.currentTimeMillis() - countDownStamp) / 1000
                    + "] there is no permits, thread id =  "
                    + Thread.currentThread().getId());
            return;
        }
        try {
            sem.tryAcquire(1, 3000, TimeUnit.MILLISECONDS);
            try {
                Thread.sleep(1000);
                int now = count.incrementAndGet();
                System.out.println("[" + (System.currentTimeMillis() - countDownStamp) / 1000 + "]"
                        + Thread.currentThread().getId() + " :run count=" + now);
            } finally {
                sem.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
