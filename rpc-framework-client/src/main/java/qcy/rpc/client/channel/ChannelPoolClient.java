package qcy.rpc.client.channel;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by quanchengyun on 2018/5/29.
 */
public class ChannelPoolClient {

    //since Nio channel is not  thread safe ,it's bounded the one specific selector if there has multiple selector
    //e.g multiple  NioEventLoop

    Map<String,NioSocketChannel> channelPartition;

    public Future send(String host, String body){
        return null;
    }


}
