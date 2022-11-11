package com.atguigu.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupCharServer {
    //定义相关属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;
    
    //构造器
    //初始化
    public GroupCharServer(){
        try {
            //得到选择器
            selector = Selector.open();
            //初始化ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将listen 注册到selector 事件为 accept
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    //监听代码
    public void  listen(){
        try {
            //循环监听
            while (true){
                int count = selector.select();
                if (count>0){//有事件处理
                    //遍历 selected KEY
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        //获取到有事件的key
                        SelectionKey key = iterator.next();
                        //不同事件不同处理
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //将sc注册到selector
                            sc.register(selector,SelectionKey.OP_READ);
                            System.out.printf(sc.getRemoteAddress() + "上线了...\n");
                        }
                        if(key.isReadable()){//通道发送read事件,可读的状态
                            //处理读（单独方法）
                            readData(key);
                        }
                        //当前key从 selected删除，防止重复处理
                        iterator.remove();
                    }

                }else{
                    System.out.println("等待中。。。");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            
        }
        
    }

    //读取数据方法
    private void readData(SelectionKey key){
        //获取socket channel
        SocketChannel channel = null;
        try {
            //取到关联channel
            channel = (SocketChannel) key.channel();
            //创建缓冲buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            if(count>0){//有数据
                //把缓冲区转成字符串
                String msg = new String(buffer.array());
                System.out.println("客户端消息："+msg.trim());
                //向其他客户端发送消息（转发方法）
                sendInfoToOtherClients(msg,channel);
            }

        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress()+" 离线了...");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void sendInfoToOtherClients(String msg,SocketChannel self) throws IOException {

        //System.out.println("服务器转发消息中。。。");
        //所有注册到selector上的socket channel
        for (SelectionKey key:selector.keys()){
            Channel targetChannel = key.channel();
            //排除自己
            if(targetChannel instanceof SocketChannel && targetChannel != self){
                //转型
                SocketChannel dest = (SocketChannel)targetChannel;
                //将msg存储到 buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer写入通道中
                dest.write(buffer);
            }

        }


    }
    
    public static void main(String[] args) {
        //启动客户端
        GroupCharServer chatServer = new GroupCharServer();

        chatServer.listen();
    }
}
