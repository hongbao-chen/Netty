package com.atguigu.netty.groupchat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个channel组,管理所有channel  <static>
    //GlobalEventExecutor.INSTANCE :全局的事件执行器，单例;
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    DateTimeFormatter dateTimeFormatter =DateTimeFormatter.ISO_DATE_TIME;

    //handlerAdded表示建立连接,一旦连接，第一个执行
    //将当前channel加入到
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        //将该该客户加入聊天；消息推送给其他在线的用户
        channelGroup.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            dateTimeFormatter.format(LocalDateTime.now());
    }
}
