package com.zhaolu.study.netty.nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/12/1.
 */
public class Test {

    public static final int PORT = 8080;
    public static final int TIMEOUT = 3000;
    public static final int BUF_SIZE = 1024;

    public static void client() {

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));

            if (socketChannel.finishConnect()) {
                int i = 0;
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    String info = "I'm " + i++ + "-th information from client";
                    buffer.clear();
                    buffer.put(info.getBytes());
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        System.out.println(buffer);
                        socketChannel.write(buffer);
                    }
                    if (i > 5) {
                        break;
                    }
                }
                socketChannel.shutdownOutput();
                socketChannel.shutdownInput();
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void server() {
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            int recvMsgSize = 0;
            byte[] recvBuf = new byte[1024];
            while (true) {
                Socket clntSocket = serverSocket.accept();
                SocketAddress clientAddress = clntSocket.getRemoteSocketAddress();
                System.out.println("Handling client at "+clientAddress.toString());
                try (InputStream in = clntSocket.getInputStream()) {
                    while ((recvMsgSize = in.read(recvBuf)) != -1) {
                        byte[] temp = new byte[recvMsgSize];
                        System.arraycopy(recvBuf,0,temp,0,recvMsgSize);
                        System.out.println(new String(temp));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void selector() {
        try(Selector selector = Selector.open(); ServerSocketChannel ssc = ServerSocketChannel.open()) {
            ssc.socket().bind(new InetSocketAddress(PORT));
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                if (selector.select(TIMEOUT) == 0) {
                    System.out.println("==");
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isValid()&&key.isAcceptable()) {
                        handleAccept(key);
                        System.out.println("accept");
                    }
                    if (key.isValid()&&key.isReadable()) {
                        handleRead(key);
                        System.out.println("read");
                    }

                    if (key.isValid()&&key.isWritable()) {
                        handleWrite(key);
                        System.out.println("write");
                    }
                    if (key.isValid()&&key.isConnectable()) {
                        System.out.println("isConnectable = true");
                    }
                    iterator.remove();
                    System.out.println("remove");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理连接接入
     * @param key
     * @throws IOException
     */
    public static void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssChannel.accept();
        sc.configureBlocking(false);
        sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(BUF_SIZE));
    }

    /**
     * 处理读取操作
     * @param key
     * @throws IOException
     */
    public static void handleRead(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        int bytesRead = sc.read(buf);
        while (bytesRead > 0) {
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.print((char)buf.get());
            }
            System.out.println();
            buf.clear();
            bytesRead = sc.read(buf);
        }
        if (bytesRead == -1) {
            sc.close();
        }
    }

    /**
     * 处理写入操作
     * @param key
     * @throws IOException
     */
    public static void handleWrite(SelectionKey key) throws IOException {
        ByteBuffer buf = (ByteBuffer) key.attachment();
        buf.flip();
        SocketChannel sc = (SocketChannel) key.channel();
        while (buf.hasRemaining()) {
            sc.write(buf);
        }
        buf.compact();
    }

    public static void main(String[] args) {
        selector();
    }
}
