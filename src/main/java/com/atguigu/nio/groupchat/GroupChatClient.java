package com.atguigu.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GroupChatClient {
    //定义属性(写入配置)
    private final static String HOST = "127.0.0.1";
    private final static int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    //初始化
    public GroupChatClient() throws IOException {
        selector = Selector.open();
        //连接服务器
        socketChannel = SocketChannel.open();
        socketChannel.socket().connect(new InetSocketAddress(HOST,PORT));
        socketChannel.configureBlocking(false);
        //将channel注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        userName = String.valueOf(socketChannel.getLocalAddress());
        System.out.println("客户端准备好了...");
    }

    //向服务器发送消息
    public  void sendInfo(String info){
        info = userName+"说:"+info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (Exception e){

        }
    }

    //读取服务器消息
    public void readInfo(){
        try {
            int readChannels = selector.select();
            if(readChannels>0){//有可用通道
                //获取可用通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if(key.isReadable()){
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        SocketChannel channel = (SocketChannel)key.channel();
                        int count = channel.read(buffer);
                        if(count>0){//有消息
                            String msg = new String(buffer.array());
                            System.out.println(msg.trim());
                        }
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        var ref = new Object() {
            GroupChatClient chatClient = new GroupChatClient();
        };
        //启动一个线程 读取数据
        new Thread(()->{
            while (true){

                ref.chatClient.readInfo();
                try {
                    if(!ref.chatClient.socketChannel.socket().getKeepAlive()){
                        System.out.println("连接中断了...尝试重连");
                        ref.chatClient = new GroupChatClient();
                    }
                    TimeUnit.SECONDS.sleep(3);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        //发送数据给服务端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String msg = scanner.nextLine();
            ref.chatClient.sendInfo(msg);
        }

    }

}
