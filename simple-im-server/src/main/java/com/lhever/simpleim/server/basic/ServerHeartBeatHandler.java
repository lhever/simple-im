package com.lhever.simpleim.server.basic;

import com.lhever.simpleim.common.msg.PingPong;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Sunnick on 2019/1/27/027.
 */
@ChannelHandler.Sharable
public class ServerHeartBeatHandler extends SimpleChannelInboundHandler<PingPong> {

    private static Logger logger = LoggerFactory.getLogger(ServerHeartBeatHandler.class);

    private static ServerHeartBeatHandler instance = new ServerHeartBeatHandler();
    private ServerHeartBeatHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PingPong ping) throws Exception {
        logger.info("(服务端) <--- {}", ping);
        logger.info("(服务端) ---> {}", PingPong.PONG);
        ctx.writeAndFlush(PingPong.PONG);
    }

    public static ServerHeartBeatHandler getInstance() {
        return instance;
    }
}
