package cn.niceabc.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 同步阻塞io
 * 一个线程只能处理一个客户端连接，客户端暴增可能会造成服务端线程资源耗尽。
 * */
public class Server {
    private static Logger log = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket server = new ServerSocket(port);

        while (true) {
            log.debug("server is start on port: {}", port);
            Socket socket = server.accept();
            new Thread(new TimeServerHandler(socket)).start();
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
