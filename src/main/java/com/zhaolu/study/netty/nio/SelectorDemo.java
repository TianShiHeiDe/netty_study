package com.zhaolu.study.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2017/11/29.
 */
public class SelectorDemo {

    public static void main(String[] args) {
        try {
            Selector open = Selector.open();
            try(SocketChannel socketChannel = SocketChannel.open()){
                socketChannel.configureBlocking(false);
                socketChannel.connect(new InetSocketAddress("https://www.baidu.com", 80));
                socketChannel.register(open, SelectionKey.OP_READ);

                while (true) {
                    int ready = open.select();
                    if (ready == 0) {
                        continue;
                    }

                    Set<SelectionKey> selectionKeys = open.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();

                        if (next.isAcceptable()) {
                            System.out.println("accept");
                        } else if (next.isConnectable()) {

                            System.out.println("connect");
                        } else if (next.isReadable()) {

                            System.out.println("read");
                        } else if (next.isWritable()) {

                            System.out.println("write");
                        }
                        iterator.remove();
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
