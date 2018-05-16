package cn.niceabc.netty.hello;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeServiceHandler extends ChannelHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(TimeServiceHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];

        buf.readBytes(req);
        String body = new String(req, "utf-8");

        log.debug("the server receive order: {}", body);

        if ("query time order".equals(body)) {
            ctx.write(Unpooled.copiedBuffer("20180515".getBytes()));
        } else {
            ctx.write(Unpooled.copiedBuffer("unknown order".getBytes()));
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
