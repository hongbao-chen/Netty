package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;
import jdk.swing.interop.SwingInterOpUtils;

import javax.tools.JavaCompiler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


/**
 * 1.自定义handler,需要继承某个netty的handlerAdapter
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据（这里可以读取客户端数据）
     * ChannelHandlerContext上下文对象，含有管道pipeline,通道channel
     * @param ctx 上下文对象，含有管道pipeline,通道channel
     * @param msg 客户端发送的数据，默认是object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //耗时业务：可以异步执行，提交到对应channel的NioEventLoop 的taskQueue中

        //方案一：用户自定义普通任务 taskQueue中 一个一个执行
//        ctx.channel().eventLoop().execute(()->{
//            try {
//                TimeUnit.SECONDS.sleep(10);
//                ctx.writeAndFlush(Unpooled.copiedBuffer("Hello 客户端", CharsetUtil.UTF_8));
//            } catch (InterruptedException e) {
//                System.out.println("发生异常："+e.getMessage());
//            }
//        });
        System.out.println("go on...");

        //方案二：用户自定义定时任务 也排队（和task一样排，按顺序）
//        ctx.channel().eventLoop().schedule(()->{
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                ctx.writeAndFlush(Unpooled.copiedBuffer("Hello 客户端 schedule", CharsetUtil.UTF_8));
//            } catch (InterruptedException e) {
//                System.out.println("发生异常："+e.getMessage());
//            }
//        },1,TimeUnit.SECONDS);

        //方案三:维护channel集合，操作


//        System.out.println("server ctx ="+ctx);
//        System.out.println("查看channel和 pipeline的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();//本质是双向链表 出栈入栈
//
//        //将msg数据转成bytebuffer
//        //ByteBuf是netty封装的Nio的byteBuffer;
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送消息是："+ buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址："+ctx.channel().remoteAddress());

    }

    //数据读取完毕后的操作
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存并刷新;
        //一般对数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello 客户端~", CharsetUtil.UTF_8));

    }

    //处理异常


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }
}
