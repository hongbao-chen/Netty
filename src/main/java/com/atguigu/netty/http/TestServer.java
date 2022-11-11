package com.atguigu.netty.http;

import com.atguigu.netty.simple.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {
    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            //创建服务器端启动对象 配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//设置NioServerSocketChannel为服务器通道
                    .option(ChannelOption.SO_BACKLOG,128)//设置线程队列等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                    .childHandler(new TestServerInitializer());
            //绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(8091).sync();
            ChannelFuture channelFuture2 = serverBootstrap.bind(8092).sync();
            //监听关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            //优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
