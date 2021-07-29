package com.lhever.simpleim.server.handler.business;

import com.alibaba.fastjson.JSON;
import com.lhever.simpleim.common.msg.MessageReq;
import com.lhever.simpleim.common.msg.MessageResp;
import com.lhever.simpleim.common.util.SessionUtil;
import com.lhever.simpleim.server.util.ServerSendUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;


public class ServerMessageHandler extends SimpleChannelInboundHandler<MessageReq> {

    private static Logger logger = LoggerFactory.getLogger(ServerMessageHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageReq msg) throws Exception {

        String sendId = SessionUtil.getUserIdByChannel(ctx.channel());

        //拿到接收方的信息
        String targetId = msg.getTargetId();
        //toUserId不为空，则私聊；为空则发广播
        if (!StringUtil.isNullOrEmpty(targetId)) {
            p2pChat(sendId, msg);
        } else {
//            broadcast(msg);
        }


    }

    /**
     * 给所有人群发消息
     */
    private void broadcast(MessageResp response) {
        Channel channel = null;
        //获取所有channel，遍历
        Map<String, Channel> sessions = SessionUtil.getAllSession();
        for (Map.Entry<String, Channel> entry : sessions.entrySet()) {
            channel = entry.getValue();
            logger.info("发送给客户端{}：{}", entry.getKey(), JSON.toJSONString(response));
            ServerSendUtils.write2Channel(response, channel);
        }
    }

    /**
     * 点对点私聊
     */
    private void p2pChat(String sendId, MessageReq messageReq) {
        String targetId = messageReq.getTargetId();
        Channel targetChannel = SessionUtil.getChannelByUserId(targetId);

        MessageResp resp = new MessageResp();
        resp.setId(messageReq.getId());
        resp.setSendId(sendId);
        resp.setTargetId(targetId);
        resp.setMessage(messageReq.getMessage());
        resp.setCreateTime(new Date());

        if (targetChannel != null) {
            ServerSendUtils.write2Channel(resp, targetChannel);
            logger.info("发送消息给客户端{}，内容是:{}", targetId, JSON.toJSONString(resp));
        } else {
            ServerSendUtils.write2RouterByKafka(targetId, resp);
        }
    }




}
