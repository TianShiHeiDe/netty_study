package com.zhaolu.study.netty.aio.server;

import java.util.Objects;

public class AsyncTimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if (Objects.nonNull(args) && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                //采用默认值
            }
        }
        AsyncTimeServerHandler timeServerHandler = new AsyncTimeServerHandler(port);
        new Thread(timeServerHandler, "AIO-AsyncTimeServerHandler-001").start();
    }
}
