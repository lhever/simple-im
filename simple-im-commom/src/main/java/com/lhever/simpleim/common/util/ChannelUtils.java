package com.lhever.simpleim.common.util;

import com.lhever.simpleim.common.msg.Msg;
import com.lhever.simpleim.common.codec.NettyCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;


public class ChannelUtils {
    public static void writeAndFlush(Channel channel, Msg msg){
        ByteBuf buf = NettyCodeC.getInstance().encode(msg);
        channel.writeAndFlush(buf);
    }
}
