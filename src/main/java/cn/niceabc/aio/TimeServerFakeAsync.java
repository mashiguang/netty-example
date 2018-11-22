package cn.niceabc.aio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 伪异步io
 * 这个和异步一点关系都没有，只是服务端使用了线程池，可以防止服务端线程暴增。
 * 通信过程还是同步阻塞的。
 *
 * 可以使用客户端cn.niceabc.socket.bio.TimeClient
 * */
public class TimeServerFakeAsync {
    private static Logger log = LoggerFactory.getLogger(TimeServerFakeAsync.class);

    public static void main(String[] args) throws IOException {

        ExecutorService exe = Executors.newFixedThreadPool(10);

        int port = 8080;
        ServerSocket server = new ServerSocket(port);

        while (true) {
            log.debug("server is start on port: {}", port);
            Socket socket = server.accept();
            exe.execute(new TimeServerHandler(socket));
        }

    }

    private static class TimeServerHandler implements Runnable {
        private Socket socket;

        public TimeServerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                while (true) {

                    String body = in.readLine();
                    if (body == null) break;
                    if ("query time order".equalsIgnoreCase(body)) {
                        out.println("20181113");
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    log.debug("socket is closed.");
                } catch (IOException e) {
                    e.printStackTrace();
                    socket = null;
                }
            }
        }
    }
}
