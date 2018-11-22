package cn.niceabc.aio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncServerHandler> {
    private static Logger log = LoggerFactory.getLogger(AcceptHandler.class);

    @Override
    public void completed(AsynchronousSocketChannel channel, AsyncServerHandler serverHandler) {
        Server.client_count++;
        log.debug("client count: {}", Server.client_count);

        serverHandler.assc.accept(serverHandler, this);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer, buffer, new ReadHandler(channel));
    }

    @Override
    public void failed(Throwable exc, AsyncServerHandler serverHandler) {
        exc.printStackTrace();
        serverHandler.latch.countDown();
    }
}
