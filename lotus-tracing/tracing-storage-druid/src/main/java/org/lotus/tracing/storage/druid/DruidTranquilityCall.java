package org.lotus.tracing.storage.druid;

import org.lotus.storage.druid.entity.ServiceMetric;
import org.lotus.storage.druid.realtime.DruidCallback;
import org.lotus.storage.druid.realtime.DruidRealtimeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin2.Call;
import zipkin2.Callback;
import zipkin2.Span;

import java.io.IOException;
import java.util.List;

/**
 * Created by quanchengyun on 2018/11/12.
 */
public class DruidTranquilityCall<V> extends Call<V> {

    private Logger log = LoggerFactory.getLogger(DruidSpanConsumer.class);

    private DruidRealtimeSender sender;

    List<Span> spans;

    public DruidTranquilityCall(DruidRealtimeSender sender,List<Span> spans){

    }

    @Override
    public V execute() throws IOException {
        for(zipkin2.Span span:spans){
            //String server = zipkin2.Span.Kind.SERVER.name();
            if (zipkin2.Span.Kind.SERVER.equals(span.kind())){
                ServiceMetric metric = new ServiceMetric();
                metric.setDuration(span.durationAsLong()/1000);
                int success = "success".equals(span.tags().get("status"))?1:0;
                metric.setSuccess(success);
                metric.setHost(span.localEndpoint().ipv4());
                metric.setServiceName(span.localServiceName());
                metric.setSpanName(span.name());
                metric.setTimestamp(span.timestampAsLong()/1000);
                //metric.set
                sender.sendAsync(metric, "service-span-metric", new DruidCallback() {
                    @Override
                    public void success() {
                        //log.info("----success store to druid---");
                    }
                    @Override
                    public void failed() {
                        log.warn("----failed store to druid---");
                    }
                });
            }
        }
        return null;
    }

    @Override
    public void enqueue(Callback<V> callback) {

    }

    @Override
    public void cancel() {
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call<V> clone() {
        return null;
    }
}
