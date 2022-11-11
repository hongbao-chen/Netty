package com.atguigu.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.FutureTask;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //创建bossGroup 和 WorkerGroup
        //说明：
        //1.创建了两个线程组,bossGroup 和WorkerGroup
        //2.bossGroup只会处理连接请求;真正的业务处理，会交给WorkerGroup完成；
        //3.两个都会无限循环（轮询）
        //4.含有的子线程个数：cpu核数*2
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(1);

        try {

            //创建服务器端启动对象 配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//设置NioServerSocketChannel为服务器通道
                    .option(ChannelOption.SO_BACKLOG,128)//设置线程队列等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道，初始化对象
                        //向pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("客户端SocketChannel hashCode="+ch.hashCode());//可以维护hash和channel集合,之后通过这个进行通讯。
                            ch.pipeline().addLast(new NettyServerHandler());

                        }
                    });//给workerGroup 的 EventLoop 设置处理器

            //服务器准备好了，绑定端口
            System.out.println("服务器 is ready...");
            ChannelFuture cf = serverBootstrap.bind(6668).sync();//绑定一个端口，并且同步；生成一个channelFuture对象

            //给ChannelFuture 注册监听器
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("监听成功");
                    }else {
                        System.out.println("监听失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();

        }finally {
            //优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
