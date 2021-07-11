package com.lhever.simpleim.client.basic;

import com.lhever.simpleim.common.msg.PingPong;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ChannelHandler.Sharable
public class ClientHeartBeatHandler extends SimpleChannelInboundHandler<PingPong> {
    private static Logger logger = LoggerFactory.getLogger(ClientHeartBeatHandler.class);

    private static ClientHeartBeatHandler instance = new ClientHeartBeatHandler();

    private ClientHeartBeatHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PingPong pong) throws Exception {
        logger.info("[客户端] <--- {}", pong);
    }

    public static ClientHeartBeatHandler getInstance() {
        return instance;
    }
}
