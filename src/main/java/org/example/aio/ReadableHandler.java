package org.example.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadableHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsynchronousSocketChannel socketChannel;

    public ReadableHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (result != -1) {
            attachment.flip();
            byte[] data = new byte[attachment.remaining()];
            attachment.get(data);
            System.out.println("接收到的数据：" + new String(data));
            if (!attachment.hasRemaining()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                socketChannel.read(readBuffer, readBuffer, new ReadableHandler(socketChannel));
            }
        } else {
            try {
                socketChannel.close();
                System.out.println("客户端断开连接");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
