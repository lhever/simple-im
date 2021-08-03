package com.lhever.simpleim.server.handler.business;

import com.lhever.simpleim.common.consts.KafkaDataType;
import com.lhever.simpleim.common.msg.GroupMessageAck;
import com.lhever.simpleim.common.util.KafkaUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class ServerGroupMessageAckHandler extends SimpleChannelInboundHandler<GroupMessageAck> {

    private static Logger logger = LoggerFactory.getLogger(ServerGroupMessageAckHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageAck ack) throws Exception {
        KafkaUtils.sendToRouter(Objects.hash(ack.getReceiveId()), KafkaDataType.GROUP_SINGLE_MSG_ACK, ack);
    }

}
