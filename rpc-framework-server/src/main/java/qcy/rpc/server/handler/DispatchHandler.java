package qcy.rpc.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

public class DispatchHandler extends ChannelInboundHandlerAdapter {
    static Logger logger = LoggerFactory.getLogger(DispatchHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("context channelActive() invoked");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            logger.info("context channelRead() invoked,recevied msg is {}", request.content());


        }

    }

}
