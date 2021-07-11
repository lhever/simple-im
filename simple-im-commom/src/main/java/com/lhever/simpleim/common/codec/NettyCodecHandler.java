package com.lhever.simpleim.common.codec;


import com.lhever.simpleim.common.msg.Msg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

@ChannelHandler.Sharable
public class NettyCodecHandler extends MessageToMessageCodec<ByteBuf, Msg> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Msg msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        NettyCodeC.getInstance().encode(byteBuf,msg);
        out.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(NettyCodeC.getInstance().decode(msg));
    }
}
