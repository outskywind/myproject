package qcy.rpc.buffer;

import java.nio.ByteBuffer;

public class DirectBufferPool {
    
    private static InheritableThreadLocal<ByteBuffer> pool;

    public static InheritableThreadLocal<ByteBuffer> getPool() {
        return pool;
    }

    /**
     * 池化的使用，不释放 线程销毁时需要释放这个ByteBuffer,如何实现？ 
     * 线程池里新增的线程，运行完一个任务后，将会阻塞，直到从workQueue中获取到任务，然后进行下一次循环运行。
     * 没有任务要运行时，将会处于阻塞状态。
     * 因此线程池的线程是一直那几个复用的。
     * @return
     */
    public static ByteBuffer getBuffer() {
        ByteBuffer bb = pool.get();
        if (bb == null) {
            bb = ByteBuffer.allocateDirect(1024 * 1024);
            pool.set(bb);
        }
        return bb;
    }

}
