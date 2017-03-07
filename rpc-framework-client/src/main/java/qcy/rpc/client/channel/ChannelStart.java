package qcy.rpc.client.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 启动到指定服务端的channel
 * 
 * @author leo.quan
 *
 */
public class ChannelStart {

    Logger logger = LoggerFactory.getLogger(ChannelStart.class);
    /**
     * 这个是worker线程数，不会影响客户端到host的连接数，socket连接是由channel表示的
     *  通常最佳设置为cpu核数*2 表示cpu时间与IO时间各占一半
     */
    private static final int WORKER_THREADS = Runtime.getRuntime().availableProcessors() * 2;
    
    /**
     * 创建指定的server channel
     * 
     * @param ip
     * @param port
     * @throws InterruptedException
     */
    public void syncChannel(String host, int port) throws InterruptedException {
        // 设置worker工作线程
        NioEventLoopGroup worker = new NioEventLoopGroup(WORKER_THREADS);
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(worker);
            bs.channel(NioSocketChannel.class);
            // 绑定远程host
            bs.remoteAddress(host, port);
            // this will block
            ChannelFuture f = bs.connect().sync();

            f.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("syncChannel Exception:", e);
        }finally{
            // 关闭是关闭线程池
            worker.shutdownGracefully().sync();
        }
    }



}
