package com.atguigu.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {
    public static void main(String[] args) {
        try {
            ServerSocket socket = new ServerSocket(9091);
            Socket accept = socket.accept();
            new Thread(()->{
                try {
                    handle(accept);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void handle(Socket accept) throws IOException {
        System.out.println("==处理socket==");
        accept.close();
    }
}
