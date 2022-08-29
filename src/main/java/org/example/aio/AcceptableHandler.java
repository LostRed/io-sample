package org.example.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptableHandler implements CompletionHandler<AsynchronousSocketChannel, AIOServer> {
    @Override
    public void completed(AsynchronousSocketChannel result, AIOServer attachment) {
        System.out.println("客户端建立连接");
        attachment.serverSocketChannel.accept(attachment, this);
        //创建新的Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //异步读  第三个参数为接收消息回调的业务Handler
        result.read(buffer, buffer, new ReadableHandler(result));
    }

    @Override
    public void failed(Throwable exc, AIOServer attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
