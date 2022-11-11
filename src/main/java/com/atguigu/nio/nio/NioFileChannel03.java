package com.atguigu.nio.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel03 {
    public static void main(String[] args) throws IOException {
        //创建输入流 准备读取数据
        File file1 = new File("E:\\JavaProject\\netty\\Netty相关资料\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file1);

        //得到 输入流的channel
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        //创建buffer 缓冲区存储数据
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file1.length());
        //将channel中的数据读到 buffer中去
        //循环读
        while (true){
            byteBuffer.clear();//清空buffer(恢复 position/limit/mark/capacity的值;不然不能读了;变成0了)
            int read = inputStreamChannel.read(byteBuffer);
            System.out.println("read="+read);
            if(read == -1 ){
                break;
            }
            //写入
            writeFile(byteBuffer);
        }

        inputStreamChannel.close();
    }

    private static void writeFile(ByteBuffer byteBuffer) throws IOException {
        //创建一个输出channel
        File file2 = new File("E:\\JavaProject\\netty\\Netty相关资料\\file01-copy.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        //将buffer数据写入channel
        byteBuffer.flip();
        fileOutputStreamChannel.write(byteBuffer);

        fileOutputStreamChannel.close();
    }
}
