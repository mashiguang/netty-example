package cn.niceabc.netty.file;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.RandomAccessFile;

public class FileServerHandler extends SimpleChannelInboundHandler<String> {

    private static final String CR = System.getProperty("line.separator");

    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        File file = new File(msg);
        if (!file.exists()) {
            ctx.writeAndFlush("file not found, " + msg + CR);
        }
        if (!file.isFile()) {
            ctx.writeAndFlush("not a file, " + msg + CR);
        }

        ctx.write(file + " " + file.length() + CR);
        RandomAccessFile randomAccessFile = new RandomAccessFile(msg, "r");
        FileRegion region = new DefaultFileRegion(
                randomAccessFile.getChannel(),
                0,
                randomAccessFile.length()
        );
        ctx.write(region);
        ctx.writeAndFlush(CR);
        randomAccessFile.close();
    }
}
