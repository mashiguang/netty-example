package cn.niceabc.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 同步非阻塞io
 * 使用selector多路复用器
 * */
public class Server {
    private static Logger log = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        int port = 8080;

        log.debug("server is start on port: {}", port);
        new Thread(new TimeServerHandler(port)).start();

    }

    private static class TimeServerHandler implements Runnable {

        private Selector selector;
        private ServerSocketChannel serverSocketChannel;
        private volatile boolean closed = false;


        public TimeServerHandler(int port) {

            try {
                selector = Selector.open();
                serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        private void handleInput(SelectionKey key) throws IOException {

            if (!key.isValid()) return;

            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }

            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(buffer);
                if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                    return;
                }

                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String msg = new String(bytes);
                log.debug("received: {}", msg);

                String res = "hello " + msg;
                doWrite(sc, res);

            }
        }

        private void doWrite(SocketChannel sc, String msg) throws IOException {

            ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
            sc.write(buffer);

        }

        @Override
        public void run() {

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
                }
            }

            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
