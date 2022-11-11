package com.atguigu.nio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Scattering:将数据写入buffer时，写入buffer数组；分散
 *
 * Gathering：将数据从buffer读取时，可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        //使用 serversocketchannel 和 socketchannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到soket 并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等到客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        //messageLengh
        int messageLen = 8;

        //循环的读
        while (true){
            int byteRead = 0;
            while (byteRead<messageLen){
                long l = socketChannel.read(byteBuffers);
                byteRead +=l;
                System.out.println("byteRead="+byteRead);
                //使用流打印，查看position limit
                Stream.of(byteBuffers).map(buffer ->
                        "buffer position="+buffer.position()+",limit = "+buffer.limit()).forEach(System.out::println);
            }
            //将所有buffer反转
            Arrays.asList(byteBuffers).forEach(ByteBuffer::flip);

            //将数据显示到客户端
            long byteWrite = 0;
            while (byteWrite<messageLen){
                long l = socketChannel.write(byteBuffers);
                byteWrite +=l;
            }
            //将所有buffer clear
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());
            System.out.println("byteRead="+byteRead+" bytewrite="+byteWrite);
        }



    }
}
