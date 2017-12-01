package com.zhaolu.study.netty.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 2017/11/29.
 */
public class FileChannelDemo {
    public static void main(String[] args) throws IOException {

        RandomAccessFile file = new RandomAccessFile("F:\\studyCode\\netty_study\\src\\main\\resources\\nio-data.txt", "rw");
        FileChannel channel = file.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(48);

        int read = channel.read(buffer);

        while (read != -1) {
            System.out.println("Read "+read);
            buffer.flip();

            while (buffer.hasRemaining()) {
                System.out.println((char)buffer.get());
            }
            buffer.clear();
            read = channel.read(buffer);
        }
        file.close();


    }
}
