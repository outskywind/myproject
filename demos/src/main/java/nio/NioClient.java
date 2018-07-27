package nio;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Created by quanchengyun on 2018/7/25.
 */
public interface NioClient {

    void connect();
    void close(SocketChannel channel);

    boolean isActive();

    Object sendRequest(Object obj) throws IOException;




}
