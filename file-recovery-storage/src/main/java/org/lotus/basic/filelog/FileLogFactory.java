package org.lotus.basic.filelog;


import org.lotus.basic.flume.FlumeFileLog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quanchengyun on 2018/2/9.
 */
public class FileLogFactory {

    public static volatile Map<String,FileLog> logs = new HashMap<>();

    public static <T> FileLog<T> get(String logName , Class<T> recordType,String serviceName){

            FileLog<T> log = logs.get(logName);
            if (log==null){
                synchronized (logs){
                    log = logs.get(logName);
                    if (log==null){
                        log = new FlumeFileLog<>(logName, recordType, serviceName);
                        logs.put(logName,log);
                    }
                }
            }
            return log;
    }




}
