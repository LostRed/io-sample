package org.example.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AIOServer {
    private final int port = 8082;
    public final CountDownLatch latch = new CountDownLatch(1);
    public AsynchronousServerSocketChannel serverSocketChannel;

    public void receiving() {
        try {
            this.serverSocketChannel= AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.accept(this, new AcceptableHandler());
            latch.await();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        AIOServer aioServer = new AIOServer();
        System.out.println("AIOServer startup");
        aioServer.receiving();
    }
}
