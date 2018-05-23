package cn.niceabc.netty.marshalling;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends ChannelHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(ClientHandler.class);

    public ClientHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for (int i = 0; i < 10; i++) {
            Req req = new Req();
            req.setId(i);
            req.setName("name"+i);
            req.setIntro("intro"+i);

            ctx.write(req);
        }

        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Req req = (Req) msg;
        log.debug("req: {},{}", req.getId(), req.getIntro());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        log.error(cause.getMessage());

        ctx.close();
    }
}
