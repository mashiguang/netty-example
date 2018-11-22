package cn.niceabc.aio.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private static Logger log = LoggerFactory.getLogger(ReadHandler.class);

    private AsynchronousSocketChannel channel;
    private CountDownLatch latch;

    public ReadHandler(AsynchronousSocketChannel channel, CountDownLatch latch) {
        this.channel = channel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {

        attachment.flip();

        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);

        String msg = new String(bytes);
        log.debug("received: {}", msg);

        latch.countDown();
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            channel.close();
            latch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
