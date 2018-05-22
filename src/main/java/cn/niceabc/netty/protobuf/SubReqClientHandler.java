package cn.niceabc.netty.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SubReqClientHandler extends ChannelHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(SubReqClientHandler.class);

    private int couter;
    private byte[] req;

    private String lineSeparator = "$_";

    public SubReqClientHandler() {
        /*byte[] req = "query time order".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);*/

        req = ("query time order"+lineSeparator).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //ctx.writeAndFlush(firstMessage);

        for (int i = 0; i < 10; i++) {
            ctx.write(subReq(i));
        }

        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug(msg.toString());
    }

    private SubscribeReqProto.SubscribeReq subReq(int i) {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();

        builder.setSubReqID(i);
        builder.setUserName("Jack");
        builder.setProductName("Phone");

        List<String> address = new ArrayList<String>();
        address.add("Beijing");
        address.add("Nanjing");
        address.add("Shanghai");

        builder.addAllAddress(address);

        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        log.error(cause.getMessage());

        ctx.close();
    }
}
