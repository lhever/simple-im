package com.lhever.simpleim.server.handler.business;

import com.lhever.simpleim.common.consts.KafkaDataType;
import com.lhever.simpleim.common.dto.kafka.KafkaGroupSingleMessageAck;
import com.lhever.simpleim.common.msg.GroupSingleMessageAck;
import com.lhever.simpleim.common.util.KafkaUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class ServerGroupSingleMessageAckHandler extends SimpleChannelInboundHandler<GroupSingleMessageAck> {

    private static Logger logger = LoggerFactory.getLogger(ServerGroupSingleMessageAckHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupSingleMessageAck ack) throws Exception {
        KafkaGroupSingleMessageAck kafkaAck = new KafkaGroupSingleMessageAck();
        kafkaAck.setGroupId(ack.getGroupId());
        kafkaAck.setGroupMsgId(ack.getGroupMsgId());
        kafkaAck.setReceiveId(ack.getReceiveId());
        kafkaAck.setUserGroupMsgId(ack.getUserGroupMsgId());

        KafkaUtils.sendToRouter(Objects.hash(ack.getReceiveId()), KafkaDataType.GROUP_SINGLE_MSG_ACK, ack);
    }

}
