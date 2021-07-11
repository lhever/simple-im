package com.lhever.simpleim.client.business;

import com.lhever.simpleim.common.msg.MessageResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientMessageHandler extends SimpleChannelInboundHandler<MessageResp> {
    private static Logger logger = LoggerFactory.getLogger(ClientMessageHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResp msg) throws Exception {
        logger.info("收到{}的消息：{}", msg.getFromUserId(), msg.getMessage());
    }


}
