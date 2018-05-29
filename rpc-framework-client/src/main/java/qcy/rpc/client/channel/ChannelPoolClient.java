package qcy.rpc.client.channel;

import java.util.concurrent.Future;

/**
 * Created by quanchengyun on 2018/5/29.
 */
public class ChannelPoolClient {

    //since Nio channel is not  thread safe ,it's bounded the one specific selector if there has multiple selector
    //e.g multiple  NioEventLoop

    public Future send(){
        return null;
    }


}
