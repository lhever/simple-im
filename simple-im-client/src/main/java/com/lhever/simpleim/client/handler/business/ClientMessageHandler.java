package com.lhever.simpleim.client.handler.business;

import com.lhever.common.core.utils.StringUtils;
import com.lhever.simpleim.common.msg.MessageAck;
import com.lhever.simpleim.common.msg.MessageResp;
import com.lhever.simpleim.common.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientMessageHandler extends SimpleChannelInboundHandler<MessageResp> {
    private static Logger logger = LoggerFactory.getLogger(ClientMessageHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResp msg) throws Exception {
        String userId = LoginUtil.getUserId(ctx.channel());
        if (StringUtils.equals(userId, msg.getReceiveId())) {
            logger.error("{}不是正确的消息接收者", userId);
        }
        logger.info("用户:{}收到:{}发送过来的消息， 内容是:{} -> {}", userId, msg.getSendId(), msg.getId(),  msg.getMessage());

        MessageAck ack = new MessageAck();
        ack.setMsgId(msg.getId());
        ack.setReceiveId(msg.getReceiveId());
        ctx.writeAndFlush(ack);


    }


}
