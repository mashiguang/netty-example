package cn.niceabc.aio.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

class AsyncClientHandler implements Runnable {
    private static Logger log = LoggerFactory.getLogger(AsyncClientHandler.class);
    public AsynchronousSocketChannel socketChannel;
    public CountDownLatch latch;

    public AsyncClientHandler() {
        try {
            socketChannel = AsynchronousSocketChannel.open();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        latch = new CountDownLatch(1);

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080)
                , null, new CompletionHandler<Void, Object>() {
                    @Override
                    public void completed(Void result, Object attachment) {
                        ByteBuffer buffer = ByteBuffer.wrap("Tom".getBytes());
                        socketChannel.write(buffer, buffer, new WriteHander(socketChannel, latch));
                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        exc.printStackTrace();

                        try {
                            socketChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
