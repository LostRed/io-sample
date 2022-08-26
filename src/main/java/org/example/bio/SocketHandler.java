package org.example.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketHandler extends Thread {
    private final Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            int length;
            byte[] data = new byte[1024];
            while ((length = inputStream.read(data)) != -1) {
                System.out.println("接收到的数据：" + new String(data, 0, length));
            }
            System.out.println("客户端断开连接");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
