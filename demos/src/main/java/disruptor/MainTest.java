package disruptor;

public class MainTest {

    public static void main(String[] args) {
        // 如果cpu为4核，4个线程运行，如果有一个线程运行无阻塞时，那么是否一定会导致CPU被这个线程完全占用？
        // ------ CPU 线程调度 ----机制
        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(new RunNothing());
            t.start();
        }

    }

    public static class RunNothing implements Runnable {

        @Override
        public void run() {
            int i = 0;
            while (true) {
                i++;
            }
        }
    }

}
