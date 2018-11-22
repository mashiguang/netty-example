package cn.niceabc.aio.client;

public class Client {

    public volatile static int client_count = 0;

    public static void main(String[] args) {

        new Thread(new AsyncClientHandler()).start();

    }


}
