package org.lotus.tracing.storage.druid;

import org.lotus.storage.druid.realtime.DruidRealtimeSender;
import zipkin2.Call;
import zipkin2.Span;
import zipkin2.storage.SpanConsumer;

import java.util.List;

/**
 * Created by quanchengyun on 2018/11/12.
 */
public class DruidSpanConsumer implements SpanConsumer {

    private DruidRealtimeSender sender;

    @Override
    public Call<Void> accept(List<Span> spans) {
         return new DruidTranquilityCall<>(sender,spans);
    }

    public void setSender(DruidRealtimeSender sender) {
        this.sender = sender;
    }
}
