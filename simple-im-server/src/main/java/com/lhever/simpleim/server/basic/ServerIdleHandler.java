package com.lhever.simpleim.server.basic;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServerIdleHandler extends IdleStateHandler {
    private static Logger logger = LoggerFactory.getLogger(ServerIdleHandler.class);

    private static int HERT_BEAT_TIME = 50;

    public ServerIdleHandler() {
        super(0, 0, HERT_BEAT_TIME);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        logger.info("no heart beat in {} seconds, close channel", HERT_BEAT_TIME);
        ctx.channel().close();
    }
}
