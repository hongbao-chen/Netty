package com.atguigu.nio.nio;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel01 {
    public static void main(String[] args) throws IOException {

        String str = new String("Hello World!");
        //创建buffer 存储缓冲数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());
        //反转buffer(写-》读)
        byteBuffer.flip();


        //创建输出流
        FileOutputStream outputStream = new FileOutputStream("E:\\JavaProject\\netty\\Netty相关资料\\file01.txt");
        //通过输出流创建channel
        FileChannel channel = outputStream.getChannel();

        //将buffer数据写入 channel
        int write = channel.write(byteBuffer);
        System.out.println(write);
        channel.close();
    }
}
