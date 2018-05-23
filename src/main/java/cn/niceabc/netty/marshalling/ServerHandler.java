package cn.niceabc.netty.marshalling;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler extends ChannelHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Req req = (Req) msg;
        log.debug("req: {},{}", req.getId(), req.getIntro());

        req.setIntro("this msg is from server.");
        ctx.writeAndFlush(req);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
