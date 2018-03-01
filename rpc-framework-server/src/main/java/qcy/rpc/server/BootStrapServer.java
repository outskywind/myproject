package qcy.rpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import qcy.rpc.server.handler.DispatchHandler;

public class BootStrapServer {

    Logger logger = LoggerFactory.getLogger(BootStrapServer.class);

    private int port = 5100;

    public BootStrapServer(int port) {
        this.port = port;
    }

    /**
     * 开启 崭新的神圣的线程世界
     */
    private void start() {

        // 一个 serverChannel 难道不是应该由一个Loop线程使用么
        // 否则就要加锁，控制并发，降低性能？
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 启动server
            ServerBootstrap sbp = new ServerBootstrap();
            // 绑定reactor 模型线程组
            sbp.group(boss, worker);
            // channel ,是selector选择器与线程组沟通的通道。
            sbp.channel(NioServerSocketChannel.class);
            // 在这里添加socketChannel 的handler,将会在SocketChannel上的事件发生后被调用
            // 处理SocketChannel上的读与写事件
            // Handler是单例的
            // 在NioServerSocketChannel 读取连接请求后，创建一个sockeChannel ，再调用它上面的处理者 ServerBootstrapAcceptor ；
            //  {@link ServerBootstrapAcceptor} 将执行   child.pipeline().addLast(childHandler); 把这里注册的childHandler 注册到每一个socketChannel上
            //  childGroup.register(child);并且将socketChannel绑定到worker线程组
            
            sbp.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // http 编解码器
                    ch.pipeline().addLast(new HttpServerCodec());
                    ch.pipeline().addLast(new DispatchHandler());
                }
            });
            sbp.option(ChannelOption.SO_BACKLOG, 128);
            sbp.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 监听端口
            ChannelFuture cf = sbp.bind(this.port);
            logger.info("启动成功... on port:{}", this.port);
            ChannelFuture closeFuture = cf.sync();
            //
            closeFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("BootStrapServer start() 异常：", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


    public static void main(String[] args) {

        String _port = System.getProperty("port", "5100");
        int port = 0;
        if (StringUtils.hasText(_port)) {
            port = Integer.parseInt(_port);
        }
        if (port <= 0) {
            throw new IllegalArgumentException("port 端口号错误");
        }
        BootStrapServer server = new BootStrapServer(port);
        server.start();
    }





}
