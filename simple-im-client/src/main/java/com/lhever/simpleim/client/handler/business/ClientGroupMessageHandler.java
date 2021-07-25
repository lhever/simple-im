package com.lhever.simpleim.client.handler.business;

import com.lhever.simpleim.common.msg.GroupMessageResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientGroupMessageHandler extends SimpleChannelInboundHandler<GroupMessageResp> {
    private static Logger logger = LoggerFactory.getLogger(ClientGroupMessageHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResp msg) throws Exception {
        logger.info("收到群聊{}的消息：{}-->{}", msg.getGroupId(), msg.getFromUserName(), msg.getGroupMsg());
    }
}
