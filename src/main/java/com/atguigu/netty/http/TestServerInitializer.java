package com.atguigu.netty.http;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();
        //加入netty提供的http coder-> coder decoder
        //HttpServerCodec说明：
        //netty提供的http编解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        //增加自定义处理器
        pipeline.addLast("",new TestHttpServerHandler());
    }
}
