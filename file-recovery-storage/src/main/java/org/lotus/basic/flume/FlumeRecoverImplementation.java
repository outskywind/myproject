package org.lotus.basic.flume;

import org.apache.flume.Event;
import org.apache.flume.Transaction;
import org.codehaus.jackson.map.ObjectMapper;
import org.lotus.basic.filelog.RecoverInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * Created by quanchengyun on 2018/2/26.
 */
public class FlumeRecoverImplementation<T> implements Runnable {

    private static Logger log = LoggerFactory.getLogger(FlumeRecoverImplementation.class);

    private FileLogChannel channel;

    private static ObjectMapper json = new ObjectMapper();

    private Class<T> recordType ;

    private RecoverInterface<T> recoverInterface;

    //0 未开始 1 开始  2 结束 0->1 ,1->2,2->0 2->3 退出循环
    private volatile AtomicInteger signal = new AtomicInteger(0);
    private volatile AtomicBoolean started = new AtomicBoolean(false);

    private long  recoverDelay = 60000*10;
    //-1 结束 1 运行
    private int runnerState=0;

    public FlumeRecoverImplementation(FileLogChannel channel,Class<T> recordType){
        this.channel = channel;
        this.recordType = recordType;
    }

    private void setRecoverInterface(RecoverInterface<T> recoverInterface) {
        this.recoverInterface = recoverInterface;
    }

    public void setRecoverDelay(long recoverDelay) {
        this.recoverDelay = recoverDelay;
    }

    private void start(){
        Thread runner = new Thread(this);
        //不希望因为没有其他非daemon线程运行而退出jvm
        runner.setName("Filelog-Recover-"+this.channel.getName());
        runner.setDaemon(false);
        runner.start();
        this.runnerState=1;

    }

    public void recover(RecoverInterface recoverInterface){
        signal.compareAndSet(0,1);
        //that's ok
        if(started.compareAndSet(false,true)){
            this.setRecoverInterface(recoverInterface);
            start();
        }
    }

    public long getRemainning(){
        return this.channel.getChannelSize();
    }

    public void stopIfNeed(){
        if(this.channel.getChannelSize()<=0){
            //在main线程不能这么调用
            //Thread.currentThread().interrupt();
            this.runnerState = -1;
            if(this.channel.isOpen()){
                this.channel.stop();
            }
        }
    }

    @Override
    public void run() {
        //如果抛出了异常，直接终止线程
        while (true && this.runnerState==1) {
            //just sleep all the way and wake up every 1min to check if main signaled the flag
            //is set true to start recovery if needed.
            //there is a concurrent problem
            log.info("recover thread running..");
            try {
                if (signal.compareAndSet(1, 2)) {
                    while(true){
                        Event e;
                        //read the sourcecode
                        if (!channel.isOpen()){
                            break;
                        }
                        Transaction tx = channel.getTransaction();
                        boolean commit = true;
                        //不能使用batch提交
                        //因为batch提交的成功的那些回滚会导致重复提交
                        try {
                            //1.如果这中间while循环了查过10*mem_capacity 就会导致takeList溢出报错
                            //2.事务commit 之后只能关闭，然后重新获取事务再开始
                            tx.begin();
                            if ((e = channel.take()) == null) {
                                //全部消费完成之后退出循环
                                tx.commit();
                                tx.close();
                                break;
                            }else{
                                T event = json.readValue(e.getBody(), recordType);
                                if(!recoverInterface.recover(event)) {
                                    log.error("recovery still failed:{}", event);
                                    commit = false;
                                }
                            }
                        } catch (Exception e1) {
                            commit = false;
                            log.warn("fileLog recovery error:", e1);
                        }
                        if (!commit) {
                            tx.rollback();
                        }else{
                            tx.commit();
                        }
                        tx.close();
                    }
                }
            }catch (Exception e){
                log.error("recover thread exception",e);
                break;
            }
            log.info("total remained message count {}",channel.getChannelSize());
            //recovery finished ,wait util next time recover() be called
            signal.compareAndSet(2, 0);
            try{
                Thread.sleep(this.recoverDelay);
            }catch (InterruptedException e){
                log.warn("thread sleep interrupted.");
            }
        }
    }
}
