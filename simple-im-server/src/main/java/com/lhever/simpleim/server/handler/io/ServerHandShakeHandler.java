package com.lhever.simpleim.server.handler.io;

import com.lhever.common.core.consts.CommonConsts;
import com.lhever.simpleim.common.util.Attributes;
import com.lhever.simpleim.common.util.Session;
import com.lhever.simpleim.server.config.ServerConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/8/5 20:02
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/8/5 20:02
 * @modify by reason:{方法名}:{原因}
 */
@ChannelHandler.Sharable
public class ServerHandShakeHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(ServerHandShakeHandler.class);
    private static final ServerHandShakeHandler instance = new ServerHandShakeHandler();

    private ServerHandShakeHandler() {
    }

    public static ServerHandShakeHandler getInstance() {
        return instance;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //记录连接建立时间
        long now = System.currentTimeMillis();
        channel.attr(Attributes.HAND_SHAKE_TIME).set(now);
        logger.info("连接:{}接入, 时间:{}", channel.id(), now);
        super.channelActive(ctx);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        Channel channel = ctx.channel();
        checkLogin(channel);
    }

    private void checkLogin(Channel channel) {
        Long handShakeTime = channel.attr(Attributes.HAND_SHAKE_TIME).get();
        if (handShakeTime == null) {
            return;
        }
        long diff = System.currentTimeMillis() - handShakeTime;
        if (diff >  ServerConfig.LOGIN_TIMEOUT_SECONDS * CommonConsts.ONE_MINUTE_MILLIS) {
            Session session = channel.attr(Attributes.SESSION).get();
            if (session == null) {
                logger.info("链接:{}在[ {} ]分钟内未登陆, 被服务端强制关闭", channel.id(), ServerConfig.LOGIN_TIMEOUT_SECONDS);
                channel.close();
            }
        }

    }
}
