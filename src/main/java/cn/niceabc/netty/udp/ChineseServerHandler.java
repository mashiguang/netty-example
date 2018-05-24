package cn.niceabc.netty.udp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChineseServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private static Logger log = LoggerFactory.getLogger(ChineseServerHandler.class);

    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {

        log.debug("msg: {}", msg.content().toString(CharsetUtil.UTF_8));

        ctx.writeAndFlush(
                new DatagramPacket(Unpooled.copiedBuffer("空外一鸷鸟，河间双白鸥。",CharsetUtil.UTF_8), msg.sender())
                );
    }
}
