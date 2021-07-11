package com.lhever.simpleim.common.codec;

import com.lhever.simpleim.common.msg.Msg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public final class NettyMessageEncoder extends MessageToByteEncoder<Msg> {

    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];


    @Override
    protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf sendBuf) throws Exception {

        NettyCodeC.getInstance().encode(sendBuf, msg);
    }







}
