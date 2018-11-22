package cn.niceabc.aio.server;

public class Server {

    public volatile static int client_count = 0;

    public static void main(String[] args) {

        new Thread(new AsyncServerHandler()).start();

    }

}
