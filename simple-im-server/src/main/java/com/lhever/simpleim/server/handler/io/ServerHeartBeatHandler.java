package com.lhever.simpleim.server.handler.io;

import com.lhever.common.core.utils.StringUtils;
import com.lhever.simpleim.common.msg.PingPong;
import com.lhever.simpleim.common.util.Attributes;
import com.lhever.simpleim.server.config.ServerConfig;
import com.lhever.simpleim.server.util.RedisUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ChannelHandler.Sharable
public class ServerHeartBeatHandler extends SimpleChannelInboundHandler<PingPong> {
    private static Logger logger = LoggerFactory.getLogger(ServerHeartBeatHandler.class);

    private static ServerHeartBeatHandler instance = new ServerHeartBeatHandler();
    private ServerHeartBeatHandler(){}

    public static ServerHeartBeatHandler getInstance() {
        return instance;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PingPong ping) throws Exception {
        logger.info("(服务端) <--- {}", ping);
        logger.info("(服务端) ---> {}", PingPong.PONG);
        Channel channel = ctx.channel();
        Attribute<String> attr = channel.attr(Attributes.USER_ID);
        String userId = null;
        if (attr != null) {
            userId = attr.get();
        }
        if (StringUtils.isNotBlank(userId)) {
            logger.info("登陆用户:{}续期", userId);
            RedisUtils.set(ServerConfig.LOGIN_KEY, userId, 60);
        }

        ctx.writeAndFlush(PingPong.PONG);
    }


}
