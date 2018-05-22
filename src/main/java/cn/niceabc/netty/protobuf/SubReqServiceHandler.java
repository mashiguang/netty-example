package cn.niceabc.netty.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubReqServiceHandler extends ChannelHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(SubReqServiceHandler.class);

    private int counter;
    private String lineSeparator = "$_";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        /*ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);

        String body = new String(req, "utf-8")
                .substring(0, req.length-lineSeparator.length());*/
        /*String body = (String) msg;

        log.debug("now is: {}, the counter is {}", body, ++couter);*/

        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;

        log.debug(req.toString());

        ctx.writeAndFlush(resp(req.getSubReqID()));
    }

    private SubscribeRespProto.SubscribeResp resp(int subReqID) {
        SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();

        builder.setSubReqID(subReqID);
        builder.setRespCode(0);
        builder.setDesc("this is a desc.");

        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
