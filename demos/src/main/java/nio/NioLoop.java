package nio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by quanchengyun on 2018/7/25.
 */
public class NioLoop {

    private String nioGroupName;

    private static AtomicInteger threadNum = new AtomicInteger(0);

    private ExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return  new Thread(null,r,nioGroupName+"-"+threadNum.getAndIncrement());
        }
    });

    public NioLoop(String nioGroupName){
        this.nioGroupName = nioGroupName;
    }

    public void execute(Runnable task){
        executor.execute(task);
    }

}
