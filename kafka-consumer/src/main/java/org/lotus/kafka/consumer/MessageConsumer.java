package org.lotus.kafka.consumer;

import java.util.List;

/**
 * Created by quanchengyun on 2018/5/7.
 */
public interface MessageConsumer {

    boolean accept(List<byte[]> message);
}
