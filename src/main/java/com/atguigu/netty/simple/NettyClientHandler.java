package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //当通道就绪就会 触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client"+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,server:喵", CharsetUtil.UTF_8));
    }

    //当channel有读取事件，会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf =  (ByteBuf)msg;
        System.out.println("服务器回复消息："+byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址："+ctx.channel().remoteAddress());
    }
}