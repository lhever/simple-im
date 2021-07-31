package com.lhever.simpleim.server.handler.business;

import com.alibaba.fastjson.JSON;
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
        String targetId = messageReq.getTargetId();
        Channel targetChannel = SessionUtil.getChannelByUserId(targetId);

        //如果用户在线
        if (targetChannel != null) {
            MessageResp resp = new MessageResp();
            resp.setId(messageReq.getId());
            resp.setSendId(sendId);
            resp.setTargetId(targetId);
            resp.setMessage(messageReq.getMessage());
            resp.setCreateTime(new Date());
            ServerSendUtils.write2Channel(resp, targetChannel);
            logger.info("发送消息给客户端{}，内容是:{}", targetId, JSON.toJSONString(resp));

        } else {
            //如果用户不在线
            KafkaP2PMessage resp = new KafkaP2PMessage();
            resp.setId(messageReq.getId());
            resp.setSendId(sendId);
            resp.setReceiveId(targetId);
            resp.setMessage(messageReq.getMessage());
            resp.setCreateTime(new Date());
            KafkaUtils.sendToRouter(Objects.hash(targetId), KafkaDataType.P2P_MSG, resp);
        }
    }




}
