package com.lhever.simpleim.client.handler.io;

import com.lhever.simpleim.common.msg.PingPong;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientIdleHandler extends IdleStateHandler {

    private static Logger logger = LoggerFactory.getLogger(ClientIdleHandler.class);

    private static final int HEART_BEAT_TIME = 40;

    public ClientIdleHandler() {
        super(0, 0, HEART_BEAT_TIME);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        logger.info("[客户端] ----> {}", PingPong.PING);
        ctx.writeAndFlush(PingPong.PING);
    }


}
