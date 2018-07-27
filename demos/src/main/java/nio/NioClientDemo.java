package nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by quanchengyun on 2018/7/25.
 */
public class NioClientDemo implements NioClient {

    private NioLoop loop;

    private String host;
    private int port;
    private ChannelPool pool;
    private Selector selector;
    private AtomicBoolean isActive = new AtomicBoolean(false);

    public NioClientDemo(String host,int port) throws Exception{
        loop = new NioLoop("NioClientReactor");
        this.host = host;
        this.port = port;
        this.selector = Selector.open();
        loop.execute(() -> {
            try{
                while(true){
                    int count = selector.select();
                    if(count==0){
                        return;
                    }
                    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                    while(it.hasNext()){
                        SelectionKey key = it.next();
                        it.remove();
                        if(key.isReadable()){
                            System.out.println("read ready...");
                        }
                        if(key.isWritable()){
                            System.out.println("write ready...");
                        }
                    }

                }
            }catch(Exception e){
                e.printStackTrace();
            }

        });
    }


    @Override
    public void connect() {
        try{
            SocketChannel channel = SocketChannel.open();
            //非阻塞通道，连接建立过程非阻塞
            channel.configureBlocking(false);
            //指定连接目标主机的端口
            channel.connect(new InetSocketAddress(host,port));
            isActive.set(true);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void close(SocketChannel channel) {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isActive() {
        return isActive.get();
    }

    @Override
    public Object sendRequest(Object obj) throws IOException {
        SocketChannel channel = pool.take();
        if(channel==null){
            channel = SocketChannel.open();
        }
        //...

        return null;
    }



}
