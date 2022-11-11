package com.atguigu.nio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        //创建 serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //得到selector对象
        Selector selector = Selector.open();

        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8091));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把 serverSocketChannel 注册到selector 关心连接时间 op_accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //等待
        while (true){
            if(selector.select(1000)==0){
                System.out.println("等了1s,无事发生");
                continue;
            }
            //如果返回》0,获取到相关的select集合
            //>0,表示已获取到关注的事件
            //selector.selectedKeys() 返回关注事件的集合
            // 通过 selectedKeys 可以获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                //根据key对应通道
                if(key.isAcceptable()){//如果是 op_accept 新客户端连接
                    //该客户端生成一个 socketchannel
                    SocketChannel socketChannel = serverSocketChannel.accept();//（明确有人了，所以马上会连接）
                    System.out.println("客户端连接 生成了一个sokectchannel"+socketChannel.hashCode());
                    socketChannel.configureBlocking(false);
                    //将当前socketchannel注册到 selector;关注事件为 op_read 同时给channel关联buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //可以读的
                if(key.isReadable()){
                    //通过key反向回去 channel
                    SocketChannel channel = (SocketChannel)key.channel();
                    //获取到channel的 buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    if(channel.read(buffer) !=-1){
                        System.out.println("from客户端"+new String(buffer.array()));
                    }else {
                        System.out.println("结束");
                    }
                }
                //手动移除当前select key;防止重复操作
                iterator.remove();
            }
        }
        
    }
}
