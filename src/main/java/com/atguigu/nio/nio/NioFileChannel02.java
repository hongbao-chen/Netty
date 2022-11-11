package com.atguigu.nio.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel02 {
    public static void main(String[] args) throws IOException {
        //创建输入流
        File file= new File("E:\\JavaProject\\netty\\Netty相关资料\\file01.txt");
        FileInputStream inputStream = new FileInputStream(file);
        //通过输出流创建channel
        FileChannel channel = inputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());

        //从channel种读取数据 到buffer中
        channel.read(byteBuffer);

        //将缓冲区数据 转换成字符串
        System.out.println(new String(byteBuffer.array()));

        channel.close();

    }
}
