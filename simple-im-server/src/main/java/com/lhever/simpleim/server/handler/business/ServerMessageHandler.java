package com.lhever.simpleim.server.handler.business;

import com.alibaba.fastjson.JSON;
import com.lhever.simpleim.common.msg.MessageReq;
import com.lhever.simpleim.common.msg.MessageResp;
import com.lhever.simpleim.common.util.SessionUtil;
import com.lhever.simpleim.common.util.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class ServerMessageHandler extends SimpleChannelInboundHandler<MessageReq> {

    private static Logger logger = LoggerFactory.getLogger(ServerMessageHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageReq msg) throws Exception {

        logger.info("收到客户端消息：{}", msg.getMessage());
        //拿到消息发送方的userId
        Session session = SessionUtil.getSessionByChannel(ctx.channel());
        logger.info("发送方为：{}", JSON.toJSONString(session));
        String fromUserId = session.getUserId();
        String fromUserName = session.getUserName();

        //构造发送报文
        MessageResp response = new MessageResp();
        response.setFromUserId(fromUserId);
        response.setFromUserName(fromUserName);
        response.setMessage(msg.getMessage());


        //拿到接收方的信息
        String toUserId = msg.getTargetUserId();
        //toUserId不为空，则私聊；为空则发广播
        if (!StringUtil.isNullOrEmpty(toUserId)) {
            p2pChat(toUserId, response);
        } else {
            broadcast(response);
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
            writeMessage(response, channel);
        }
    }

    /**
     * 点对点私聊
     */
    private void p2pChat(String toUserId, MessageResp response) {
        Channel toUserChannel = SessionUtil.getChannelByUserId(toUserId);
        logger.info("发送给客户端{}：{}", toUserId, JSON.toJSONString(response));
        writeMessage(response, toUserChannel);
    }

    private void writeMessage(MessageResp response, Channel toUserChannel) {
        //写数据
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(response);
        } else {
            logger.info(" 该用户未登录，无法向他发送消息！");
        }
    }


}
