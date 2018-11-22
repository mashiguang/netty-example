package cn.niceabc.aio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

class AsyncServerHandler implements Runnable {
    private static Logger log = LoggerFactory.getLogger(AsyncServerHandler.class);
    public AsynchronousServerSocketChannel assc;
    public CountDownLatch latch;

    public AsyncServerHandler() {
        try {
            assc = AsynchronousServerSocketChannel.open();
            assc.bind(new InetSocketAddress(8080));

            log.debug("server is start on port: {}", 8080);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        latch = new CountDownLatch(1);

        assc.accept(this, new AcceptHandler());

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
