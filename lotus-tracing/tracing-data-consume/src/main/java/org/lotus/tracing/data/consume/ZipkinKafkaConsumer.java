package org.lotus.tracing.data.consume;

import org.lotus.kafka.consumer.MessageConsumer;
import org.lotus.tracing.common.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin2.storage.StorageComponent;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quanchengyun on 2018/11/12.
 */
public class ZipkinKafkaConsumer implements MessageConsumer {

    private Logger log = LoggerFactory.getLogger(ZipkinKafkaConsumer.class);
    //
    StorageComponent storage;

    @Override
    public boolean accept(List<byte[]> message) {
        List<zipkin2.Span> span2s = new ArrayList<>();
        for(byte[] msg: message){
            if(msg.length==0) continue;
            try{
                span2s.addAll(Decoder.decode(msg));
            }catch(RuntimeException e){
                log.error("exception: ",e);
            }
        }
        try{
            doAccept(span2s);
            return true;
        }catch (Throwable t){
            return false;
        }
    }

    private void doAccept(List<zipkin2.Span> span2s) throws IOException {
        storage.spanConsumer().accept(span2s).execute();
    }

    public void setStorage(StorageComponent storage) {
        this.storage = storage;
    }
}
