package org.example.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {
    private final int port = 8080;

    public void receiving() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                this.handle(socket);
                System.out.println("客户端建立连接");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handle(Socket socket) {
        SocketHandler socketHandler = new SocketHandler(socket);
        socketHandler.start();
    }

    public static void main(String[] args) {
        System.out.println("BIOServer startup");
        BIOServer bioServer = new BIOServer();
        bioServer.receiving();
    }
}
