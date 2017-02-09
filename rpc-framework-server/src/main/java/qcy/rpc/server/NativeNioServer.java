package qcy.rpc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NativeNioServer {


    public void start(int port) {
        
        //1. server socket channel,Nio2
        try {
            // channel ,default reactor group[thread pool]
            AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
            // bind InetSocketAddress, actually ,channel 就是socket 与 应用程序之间的通道。
            // channel 由绑定的socket端口，监听的事件，处理的线程组成。
            serverChannel.bind(new InetSocketAddress(port), 128);

            // worker reactor group
            // Executors.newWorkStealingPool() 用的是forkjoinpool ，是分解子任务后，再组装子任务结果，就可以获得最终结果的
            // 异步任务类型使用
            //AsynchronousChannelGroup workerGroup = AsynchronousChannelGroup
            //        .withThreadPool(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 - 1));

            while (true) {
                // create connection asynchronously
                Future<AsynchronousSocketChannel> channel = serverChannel.accept();
                //completion handler 将会与serverChannel 的 initiate thread 是同一个线程池调用
                //高并发时，如果用户连接数高，那么将会耗尽线程池可用线程。导致新的连接请求无法响应
                // serverChannel.accept(attachment, handler);
                channel.get().
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

    }

}
