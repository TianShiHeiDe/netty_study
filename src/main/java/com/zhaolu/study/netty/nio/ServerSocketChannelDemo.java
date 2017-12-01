package com.zhaolu.study.netty.nio;

/**
 * Created by Administrator on 2017/11/29.
 */
public class ServerSocketChannelDemo {

    public static void main(String[] args) {
       /* int count = 0;
        try (ServerSocketChannel s = ServerSocketChannel.open()){
            s.socket().bind(new InetSocketAddress(9999));
            while (true) {
                if (count > 5) {
                    break;
                }
                SocketChannel accept = s.accept();
                System.out.println("accept one is socket");
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
       Test.client();
    }
}
