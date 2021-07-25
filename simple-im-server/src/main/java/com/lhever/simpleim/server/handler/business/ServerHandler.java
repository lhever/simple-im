package com.lhever.simpleim.server.handler.business;

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
public class ServerHandler extends SimpleChannelInboundHandler<Msg> {

    private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);


    private static Map<Integer, SimpleChannelInboundHandler<? extends Msg>> handlerMap = new ConcurrentHashMap<>();

    static {
        handlerMap.putIfAbsent(MsgType.MESSAGE_REQUEST, new ServerMessageHandler());
        handlerMap.putIfAbsent(MsgType.CREATE_GROUP_REQUEST, new ServerCreateGroupHandler());
        handlerMap.putIfAbsent(MsgType.GROUP_MESSAGE_REQUEST, new ServerGroupMessageHandler());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Msg packet) throws Exception {
        logger.info("收到客户端请求：{}", JSON.toJSONString(packet));
        SimpleChannelInboundHandler handler = handlerMap.get(packet.getType());
        if (handler != null) {
            handler.channelRead(channelHandlerContext, packet);
        } else {
            logger.info("未找到响应指令，请确认指令是否正确:{}", JsonUtils.obj2Json(packet));
        }
    }
}
