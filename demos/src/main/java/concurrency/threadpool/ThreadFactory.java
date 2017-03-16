package concurrency.threadpool;

public class ThreadFactory {


    private ThreadFactory() {}

    public static Thread newThread(Class<? extends Thread> t) {
        try {
            return t.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }



}
