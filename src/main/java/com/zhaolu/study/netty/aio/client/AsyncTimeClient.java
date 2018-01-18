package com.zhaolu.study.netty.aio.client;

import java.util.Objects;

public class AsyncTimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (Objects.nonNull(args) && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                //采用默认值
            }
        }
        AsyncTimeClientHandler timeClientHandler = new AsyncTimeClientHandler("127.0.0.1", port);
        new Thread(timeClientHandler, "AIO-AsyncTimeClientHandler-001").start();
    }
}
