package org.lotus.basic.flume;

import org.lotus.basic.filelog.FileLog;
import org.apache.flume.Context;
import org.apache.flume.Transaction;
import org.apache.flume.channel.SpillableMemoryChannel;
import org.apache.flume.channel.file.FileChannelConfiguration;
import org.apache.flume.event.JSONEvent;
import org.codehaus.jackson.map.ObjectMapper;
import org.lotus.basic.filelog.RecoverInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by quanchengyun on 2018/2/9.
 */
public class FlumeFileLog<T> implements FileLog<T> {

    Logger log = LoggerFactory.getLogger(FlumeFileLog.class);

    private FileLogChannel channel;

    private String logName;

    private String serviceName;

    private FlumeRecoverImplementation recoverImplementation;
    //0 未初始化 1 初始化中 2 完成 3 失败
    private volatile AtomicInteger status = new AtomicInteger(0);

    private static ObjectMapper json = new ObjectMapper();
    //正常情况不会无限大的队列，无界队列应该没问题
    Executor executor = Executors.newFixedThreadPool(1);

    /**
     * flume的目录限制##每一个目录只能被一个FileChannel start()时 lock锁定
     * 因此不同的FileChannel 实例必须锁定不同的目录
     * 1.在一台机器上多个fileChannel【无法实现，只能每个实例注册不同的】
     * 2.在一个应用中多个fileChannel实例
     *
     * 需要重新设计：针对逻辑上的logName 处于一个大的目录，不同的Channel 实例占有不同的子目录分区
     * 关于channel与子目录分配1.自动往后分配2.zk协调
     * 如果channel实例>现有子目录数，新分配子目录；如果 channel 数<现有子目录数
     * 1，合并 【开销巨大】； 2.分给现有channel，应该可以做好均衡？，写一个，但是读取时要同时读取全部的子目录
     *
     * @param logName
     */
    public FlumeFileLog(String logName,Class<T> recordType,String serviceName){
        this.logName = logName;
        this.serviceName = serviceName;
        this.channel  = new FileLogChannel();
        this.recoverImplementation = new FlumeRecoverImplementation(channel,recordType);
    }

    private String  getServiceName(){
        // file:/opt/server/sevend/invitecenter-consumer/invitecenter-consumer-1.0-SNAPSHOT.jar!/BOOT-INF/lib/flume-logs-1.0.0-SNAPSHOT.jar!/
        String args = System.getProperty("sun.java.command");
        //获取最开始的哪一个jar包的路径作为整个fatjar应用的名字
        String path=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.indexOf(".jar")>0){
            path = path.substring(0,path.indexOf(".jar"));
            path = path.substring(path.lastIndexOf(File.separator) + 1);
            path = path.substring(0,path.lastIndexOf("-"));
            path = path.substring(0,path.lastIndexOf("-"));
            return path;
        }
        return null;
    }

    private void start(){

        Context context = new Context();
        try{
            String homePath = System.getProperty("user.home").replace('\\', '/');
            //新的channel 路径 支持单机多应用
            /**
             * String strCheckpointDir =
             * context.getString(FileChannelConfiguration.CHECKPOINT_DIR,
             * homePath + "/.flume/file-channel/checkpoint").trim();
             * 以后再升级考虑 tomcat 等 web 容器部署的情况
             * 暂时只针对微服务部署
             */
            String dataDir;
            String checkpointDir;
            String backupCheckpointDir;
            String serviceName = this.serviceName==null?getServiceName():this.serviceName;
            if(serviceName==null){
                throw  new IllegalStateException("serviceName can't defined");
            } else{
                checkpointDir = new StringBuilder(homePath).append("/.flume/").append(serviceName).append("/").append(logName).append("/checkpoint/").toString();
                dataDir  = new StringBuilder(homePath).append("/.flume/").append(serviceName).append("/").append(logName).append("/data/").toString();
                backupCheckpointDir = new StringBuilder(homePath).append("/.flume/backup/").append(serviceName).append("/").append(logName).append("/checkpoint/").toString();
            }
            context.put(SpillableMemoryChannel.MEMORY_CAPACITY,"100");
            //1000000 is just enough for our system events quantity
            context.put(SpillableMemoryChannel.OVERFLOW_CAPACITY,"1000000");
            //if the checkpoint is incomplete due to fail-over , this enables us to use the  backup checkpoint which is complete,
            //otherwise in some case ,the full replay will fail due to failed to delete the checkpoint file,which the reason is to be discovered.
            context.put(FileChannelConfiguration.USE_DUAL_CHECKPOINTS,"true");
            context.put(FileChannelConfiguration.CHECKPOINT_DIR,checkpointDir);
            context.put(FileChannelConfiguration.DATA_DIRS,dataDir);
            context.put(FileChannelConfiguration.BACKUP_CHECKPOINT_DIR,backupCheckpointDir);
            channel.setName(logName);
            channel.configure(context);
            channel.start();
        }catch(Exception e){
            log.warn("flume file channel initialize failed:",e);
        }
    }



    public void startIfNot(){
        //未初始化
        if(status.compareAndSet(0,1)){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    start();
                    if (channel.isOpen()){
                        status.compareAndSet(1,2);
                    }else{
                        status.compareAndSet(1,3);
                        log.error("FlumeFileLog {} init failed...",logName);
                    }
                }
            });
            t.start();
        }
    }

    @Override
    public void putRecord(final T record) {
        startIfNot();
        //初始化中
        if(status.get()==1){
            //异步任务
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    int count=300;
                    while(status.get()==1 && count-->0){
                        try{
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    record(record);
                }
            });
        }
        else if(status.get()==2){
            record(record);
        }
    }

    /**
     * 记录到channel
     * @param record
     */
    public void record(T record){
        if(!channel.isOpen()){
            return;
        }
        Transaction tx;
        try{
            tx = channel.getTransaction();
            tx.begin();
            try{
                JSONEvent e =  new JSONEvent();
                byte[] body = json.writeValueAsBytes(record);
                e.setBody(body);
                channel.put(e);
                tx.commit();
            }catch (Exception e){
                tx.rollback();
            }
            tx.close();
        }catch(Exception e){
            log.error("record failed message dropped:{}",record,e);
        }
    }

    @Override
    public T takeRecord() {
        return null;
    }

    @Override
    public void recoverRecord(RecoverInterface<T> recoverInterface) {
        if(!channel.isOpen()){
            startIfNot();
        }
        recoverImplementation.recover(recoverInterface);
    }

}
