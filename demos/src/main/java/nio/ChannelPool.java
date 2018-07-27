package nio;

import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by quanchengyun on 2018/7/25.
 */
public class ChannelPool {

    private SocketChannel[] pools ;
    private AtomicInteger readIndex;
    private AtomicInteger writeIndex;

    public ChannelPool(int capacity){
        int newCap = capacity>1? capacity: 10000;
        pools = new SocketChannel[newCap];
        readIndex = new AtomicInteger(0);
        writeIndex = new AtomicInteger(0);
    }


    public boolean needMoreChannel(){
        return readIndex.get() == writeIndex.get() && writeIndex.get()>0 && writeIndex.get()<pools.length;
    }

    /**
     *
     * @return
     * @throws IllegalStateException 当channel pool 满了且无可用channel将抛出异常
     */
    public SocketChannel take() throws IllegalStateException{
        //no readable ,pool empty
        if(readIndex.get()==writeIndex.get()){
            if(pools[pools.length-1]==null){
                return null;
            }
            throw new IllegalStateException("all pool channel is in use, increase the pool capacity or try it later");
        }
        int count=32;
        int idx;
        while (count-->0){
            idx = readIndex.get();
            if(readIndex.compareAndSet(idx,(idx+1)%pools.length)){
                return pools[idx];
            }
        }
        synchronized (readIndex){
            idx= readIndex.get();
            readIndex.set((idx+1)% pools.length);
            return pools[idx];
        }
    }
    /**
     *
     * @param channel
     * @return false if ring buffer is full
     */
    public boolean put(SocketChannel channel){
        //% mod full
        if(readIndex.get()==(writeIndex.get()+1)%pools.length){
            return false;
        }
        int count=32;
        int idx;
        while (count-->0){
            idx = writeIndex.get();
            if(writeIndex.compareAndSet(idx,(idx+1)%pools.length)){
                 pools[idx]=channel;
                 return true;
            }
        }
        synchronized (writeIndex){
            idx= writeIndex.get();
            writeIndex.set((idx+1)% pools.length);
            pools[idx]=channel;
        }
        return true;
    }

}
