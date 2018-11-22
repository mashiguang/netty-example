package cn.niceabc.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 同步非阻塞io
 * 使用selector多路复用器
 * */
public class TimeClient {
    private static Logger log = LoggerFactory.getLogger(TimeClient.class);

    public static void main(String[] args) throws IOException, InterruptedException {

        TimeClientHandler client = new TimeClientHandler();
        new Thread(client).start();

        Thread.sleep(1000);

        client.sendMsg("Tom");
    }

    private static class TimeClientHandler implements Runnable {

        private Selector selector;
        private SocketChannel socketChannel;

        private volatile boolean closed = false;

        public TimeClientHandler() {

            try {
                selector = Selector.open();
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_CONNECT);

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        private void doConnect() throws IOException {
            if (socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080)));
            else socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }

        public void sendMsg(String msg) throws IOException {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel, msg);
        }

        public void close() {
            closed = true;
        }

        private void doWrite(SocketChannel sc, String msg) throws IOException {

            ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
            sc.write(buffer);

        }

        private void handleInput(SelectionKey key) throws IOException {

            if (!key.isValid()) return;

            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect());
                else System.exit(1);
            }

            if (key.isReadable()) {

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(buffer);
                if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                }

                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String msg = new String(bytes);
                log.debug("received: {}", msg);

                closed = true;
            }
        }

        @Override
        public void run() {

            try {
                doConnect();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            while (!closed) {
                try {
                    selector.select(1000);
                    Set<SelectionKey> keys = selector.selectedKeys();

                    Iterator<SelectionKey> it = keys.iterator();

                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();

                        try {
                            handleInput(key);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null) {
                                    key.channel().close();
                                }
                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }

            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
