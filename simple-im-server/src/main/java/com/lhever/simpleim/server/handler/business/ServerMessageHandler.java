package com.lhever.simpleim.server.handler.business;

import com.alibaba.fastjson.JSON;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.simpleim.common.consts.KafkaDataType;
import com.lhever.simpleim.common.dto.kafka.KafkaP2PMessage;
import com.lhever.simpleim.common.msg.MessageReq;
import com.lhever.simpleim.common.msg.MessageResp;
import com.lhever.simpleim.common.util.KafkaUtils;
import com.lhever.simpleim.common.util.SessionUtil;
import com.lhever.simpleim.server.util.ServerSendUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;


public class ServerMessageHandler extends SimpleChannelInboundHandler<MessageReq> {

    private static Logger logger = LoggerFactory.getLogger(ServerMessageHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageReq msg) throws Exception {
        String sendId = SessionUtil.getUserIdByChannel(ctx.channel());
        p2pChat(sendId, msg);
    }

    /**
     * 点对点私聊
     */
    private void p2pChat(String sendId, MessageReq messageReq) {
        String receiveId = messageReq.getReceiveId();
        Channel targetChannel = SessionUtil.getChannelByUserId(receiveId);

        String msgId = StringUtils.getUuid();

        KafkaP2PMessage kafkaP2PMessage = new KafkaP2PMessage();
        kafkaP2PMessage.setId(msgId);
        kafkaP2PMessage.setSendId(sendId);
        kafkaP2PMessage.setReceiveId(receiveId);
        kafkaP2PMessage.setMessage(messageReq.getMsg());
        kafkaP2PMessage.setCreateTime(new Date());

        //如果用户在线
        if (targetChannel != null) {
            MessageResp resp = new MessageResp();
            resp.setId(msgId);
            resp.setSendId(sendId);
            resp.setReceiveId(receiveId);
            resp.setMessage(messageReq.getMsg());
            resp.setCreateTime(new Date());
            ServerSendUtils.write2Channel(resp, targetChannel);
            logger.info("发送消息给用户:{}，内容是:{}", receiveId, JSON.toJSONString(resp));

            //用户已经收到消息，仅仅保存到数据库
            kafkaP2PMessage.setSaveOnly(true);
        }

        //即使消息已经入库，仍然要保存到数据库
        KafkaUtils.sendToRouter(Objects.hash(receiveId), KafkaDataType.P2P_MSG, kafkaP2PMessage);
    }




}
