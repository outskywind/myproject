package org.lotus.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamx.tranquility.config.PropertiesBasedConfig;
import com.metamx.tranquility.config.TranquilityConfig;
import com.metamx.tranquility.druid.DruidBeams;
import com.metamx.tranquility.partition.HashCodePartitioner;
import com.metamx.tranquility.partition.Partitioner;
import com.metamx.tranquility.tranquilizer.Tranquilizer;
import com.metamx.tranquility.typeclass.JsonWriter;
import com.metamx.tranquility.typeclass.ObjectWriter;
import com.metamx.tranquility.typeclass.Timestamper;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.joda.time.DateTime;
import org.lotus.storage.druid.entity.ServiceMetric;
import org.lotus.storage.druid.realtime.DruidRealtimeSender;
import org.lotus.storage.druid.rest.RestDruidClient;
import org.lotus.storage.zk.ZkServiceDiscovery;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by quanchengyun on 2018/5/14.
 */
@Configuration
public class DruidAutoConfiguration {

    @ConditionalOnMissingBean
    @ConditionalOnBean(ZkServiceDiscovery.class)
    @Bean("default")
    public RestDruidClient restDruidClient(AsyncHttpClient httpClient, ZkServiceDiscovery hostDiscovery){
        RestDruidClient client =  new RestDruidClient();
        client.setHttpClient(httpClient);
        client.setHostDiscovery(hostDiscovery);
        return client;
    }

    @ConditionalOnMissingBean
    @Bean
    public AsyncHttpClient asyncHttpClient(){
        AsyncHttpClient client = Dsl.asyncHttpClient();
        return client;
    }

    @Bean
    public Timestamper<ServiceMetric> initTimestamper(){
        return (Timestamper<ServiceMetric>) metric -> {
            DateTime dt = new DateTime();
            dt.withMillis(metric.getTimestamp());
            return dt;
        };
    }
    /**
     * 使用一致hash分区
     * @return
     */
    @Bean
    public Partitioner<ServiceMetric> initPartitioner(){
        return new HashCodePartitioner<ServiceMetric>();
    }

    @Bean
    public ObjectWriter<ServiceMetric> initObjectWriter(final ObjectMapper objectMapper){
        return new JsonWriter<ServiceMetric>() {
            @Override
            public void viaJsonGenerator(ServiceMetric metric, JsonGenerator jsonGenerator) {
                try {
                    objectMapper.writeValue(jsonGenerator, metric);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @ConditionalOnMissingBean
    @Bean
    public DruidRealtimeSender druidRealtimeSender(Timestamper<ServiceMetric> timestamper, ObjectWriter<ServiceMetric> objectWriter, Partitioner<ServiceMetric> partitioner){
        DruidRealtimeSender instance = new DruidRealtimeSender();

        InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("tranquility.yml");
        TranquilityConfig<PropertiesBasedConfig> config = TranquilityConfig.read(ins);
        List<String> dataSources = config.getDataSources();
        for(String dataSource: dataSources){
            Tranquilizer<ServiceMetric> sender = DruidBeams.fromConfig(config.getDataSource(dataSource),timestamper,objectWriter).partitioner(partitioner)
                    .buildTranquilizer(config.getDataSource(dataSource).tranquilizerBuilder());
            sender.start();
            instance.add(dataSource,sender);
        }
        return instance;
    }

}
