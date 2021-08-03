package com.lhever.simpleim.server.handler.business;

import com.lhever.simpleim.common.consts.KafkaDataType;
import com.lhever.simpleim.common.dto.kafka.KafkaMessageAck;
import com.lhever.simpleim.common.msg.MessageAck;
import com.lhever.simpleim.common.util.KafkaUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class ServerMessageAckHandler extends SimpleChannelInboundHandler<MessageAck> {

    private static Logger logger = LoggerFactory.getLogger(ServerMessageAckHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageAck msg) throws Exception {
        ack(msg);
    }

    /**
     * 点对点私聊
     */
    private void ack(MessageAck ack) {
        String receiveId = ack.getReceiveId();
        KafkaMessageAck kafkaAck = new KafkaMessageAck();
        kafkaAck.setMsgId(ack.getMsgId());
        kafkaAck.setReceiveId(receiveId);
        KafkaUtils.sendToRouter(Objects.hash(receiveId), KafkaDataType.P2P_MSG_ACK, kafkaAck);
    }




}
