package com.atguigu.nio.nio;

import java.io.*;
import java.nio.channels.FileChannel;

public class NioFileChannel04 {
    public static void main(String[] args) throws IOException {
        //创建输入输出流
        FileInputStream fileInputStream = new FileInputStream("E:\\JavaProject\\netty\\Netty相关资料\\file01.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\JavaProject\\netty\\Netty相关资料\\file01-copy2.txt");

        //创建channel
        FileChannel srCh = fileInputStream.getChannel();
        FileChannel destCh = fileOutputStream.getChannel();

        destCh.transferFrom(srCh,0,srCh.size());

        srCh.close();
        destCh.close();
    }
}
