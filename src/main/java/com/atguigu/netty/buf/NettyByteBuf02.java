package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyByteBuf02 {
    public static void main(String[] args) {

        //生成一个容量36，  当前writeIndex = 12的 内部类buffer
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", CharsetUtil.UTF_8);

        if(byteBuf.hasArray()){
            byte[] array = byteBuf.array();

            System.out.println("byte="+byteBuf);
            byteBuf.arrayOffset();//0
            byteBuf.readerIndex();//0
            byteBuf.writerIndex();//12
            //byteBuf.readByte();//改变read index
            int len= byteBuf.readableBytes();//得到可读长度
            for (int i = 0; i < len; i++) {
                System.out.println((char) byteBuf.getByte(i));
            }
            //读某一部分
            System.out.println(byteBuf.getCharSequence(1,3,CharsetUtil.UTF_8));
        }

    }
}
