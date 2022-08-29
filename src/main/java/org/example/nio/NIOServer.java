package org.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    private final int port = 8081;
    private final SocketAddress socketAddress = new InetSocketAddress(port);

    public void receiving() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(socketAddress);
            serverSocketChannel.configureBlocking(false);
            //创建多路复用器
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    //新连接
                    if (key.isAcceptable()) {
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("客户端建立连接");
                    }
                    //连接有可读数据
                    else if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        byte[] data = new byte[1024];
                        int length = clientChannel.read(ByteBuffer.wrap(data));
                        if (length != -1) {
                            System.out.println("接收到的数据：" + new String(data, 0, length));
                        } else {
                            clientChannel.close();
                            System.out.println("客户端断开连接");
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("NIOServer startup");
        NIOServer nioServer = new NIOServer();
        nioServer.receiving();
    }
}
