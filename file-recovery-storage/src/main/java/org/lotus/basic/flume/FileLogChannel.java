package org.lotus.basic.flume;

import org.apache.flume.channel.SpillableMemoryChannel;

/**
 * Created by quanchengyun on 2018/2/28.
 */
public class FileLogChannel extends SpillableMemoryChannel {


    public long getPutCount(){
        return channelCounter.getEventPutSuccessCount();
    }

    public long getTakeCount(){
        return channelCounter.getEventTakeSuccessCount();
    }

    public long getChannelSize(){
        return channelCounter.getChannelSize();
    }

}
