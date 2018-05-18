package cn.niceabc.netty.fixed;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServiceHandler extends ChannelHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(EchoServiceHandler.class);

    private int counter;
    private String lineSeparator = "$_";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        /*ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];

        buf.readBytes(req);
        String body = new String(req, "utf-8");*/

        String body = (String) msg;

        log.debug("receive: {}, the counter is {}", body, ++counter);

        /*String current = "unknown order";
        if ("query time order".equals(body)) {
            current = "20180516";
        }
        current += lineSeparator;

        ByteBuf resp = Unpooled.copiedBuffer(current.getBytes());
        ctx.writeAndFlush(resp);*/

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
