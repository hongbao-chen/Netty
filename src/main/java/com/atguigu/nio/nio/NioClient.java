package com.atguigu.nio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    public static void main(String[] args) throws IOException {
        //
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8091);

        if(!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接不会阻塞，可以先坐一会其他工作");
            }
        }
        String s = new String("Hello World!!!");

        ByteBuffer wrap = ByteBuffer.wrap(s.getBytes());
        //发送数据；将buffer写入channel
        socketChannel.write(wrap);
        System.in.read();

    }
}
