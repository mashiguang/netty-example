package cn.niceabc.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    private static Logger log = LoggerFactory.getLogger(Server.class);

    public void bind(int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChineseServerHandler());

            log.debug("listening on {}", port);

            b.bind(port).sync().channel().closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }


    }

    public static void main(String[] args) throws InterruptedException {
        new Server().bind(8801);
    }
}
