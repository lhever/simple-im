package com.lhever.simpleim.client.handler.business;

import com.alibaba.fastjson.JSON;
import com.lhever.simpleim.common.consts.MsgType;
import com.lhever.simpleim.common.msg.Msg;
import com.lhever.simpleim.common.util.JsonUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<Msg> {
    private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private static Map<Integer, SimpleChannelInboundHandler<? extends Msg>> handlerMap = new ConcurrentHashMap<>();

    static {
        handlerMap.putIfAbsent(MsgType.MESSAGE_RESPONSE, new ClientMessageHandler());
        handlerMap.putIfAbsent(MsgType.CREATE_GROUP_RESPONSE, new ClientCreateGroupHandler());
        handlerMap.putIfAbsent(MsgType.GROUP_MESSAGE_RESPONSE, new ClientGroupMessageHandler());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
        logger.info("收到服务器响应：{}", JSON.toJSONString(msg));
        SimpleChannelInboundHandler handler = handlerMap.get(msg.getType());
        if (handler != null) {
            handler.channelRead(ctx, msg);
        } else if (msg.getType() == MsgType.HEART_BEAT) {
            logger.info("收到心跳响应：{}", JSON.toJSONString(msg));
        } else {
            logger.info("未找到响应指令，请确认指令是否正确:{}", JsonUtils.obj2Json(msg));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("与服务端连接断开......");
    }
}
