package org.lotus.tracing.storage.conf;


import org.lotus.kafka.consumer.KafkaConfigurationProperties;
import org.lotus.kafka.consumer.KafkaConsumer;
import org.lotus.storage.druid.realtime.DruidRealtimeSender;

import org.lotus.tracing.data.consume.ZipkinKafkaConsumer;
import org.lotus.tracing.storage.druid.DruidSpanConsumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by quanchengyun on 2018/5/14.
 */
@Configuration
public class DruidAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public DruidSpanConsumer druidSpanConsumer(DruidRealtimeSender sender){
        DruidSpanConsumer instance = new DruidSpanConsumer();
        instance.setSender(sender);
        return instance;
    }

    @Bean(name="zipkin.druidKafkaConfig")
    @ConfigurationProperties(prefix="druid.kafka")
    public KafkaConfigurationProperties druidKafkaConfig(){
        return new KafkaConfigurationProperties();
    }

    @Bean(initMethod ="start",destroyMethod ="stop")
    @ConditionalOnBean(ZipkinKafkaConsumer.class)
    public KafkaConsumer kafkaConsumer(@Qualifier("zipkin.druidKafkaConfig") KafkaConfigurationProperties kafkaConfig, ZipkinKafkaConsumer druidMessageConsumer){
        KafkaConsumer consumer = new KafkaConsumer();
        consumer.setKafkaConfig(kafkaConfig);
        consumer.setMessageConsumer(druidMessageConsumer);
        return consumer;
    }

}
