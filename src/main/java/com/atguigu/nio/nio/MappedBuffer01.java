package com.atguigu.nio.nio;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedBuffer01 {
    public static void main(String[] args) throws IOException {
        //RandomAccessFile 文件对象
        RandomAccessFile accessFile = new RandomAccessFile("E:\\JavaProject\\netty\\Netty相关资料\\file01.txt", "rw");
        //创建channel
        FileChannel accessFileChannel = accessFile.getChannel();
        //通过channel创建映射 mappedbuffer
        MappedByteBuffer map = accessFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0, (byte) '0');
        map.put(1, (byte) '0');
        map.put(2, (byte) '0');
        map.put(3, (byte) '0');
        map.put(4, (byte) '0');

        accessFileChannel.close();

    }
}
