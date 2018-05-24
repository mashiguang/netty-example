package cn.niceabc.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class Client {
    private static Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {

        int port = 8801;

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                        protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {

                            String text = msg.content().toString(CharsetUtil.UTF_8);
                            log.debug("msg: {}", text);
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            cause.printStackTrace();
                            ctx.close();
                        }
                    });

            Channel ch = b.bind(0).sync().channel();
            ch.writeAndFlush(
                    new DatagramPacket(Unpooled.copiedBuffer("查询", CharsetUtil.UTF_8),
                            new InetSocketAddress("255.255.255.255",port))
            ).sync();

            if (!ch.closeFuture().await(15000)) {
                log.debug("超时");
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
