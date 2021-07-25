package com.lhever.simpleim.client.handler.business;

import com.alibaba.fastjson.JSON;
import com.lhever.simpleim.common.msg.CreateGroupResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientCreateGroupHandler extends SimpleChannelInboundHandler<CreateGroupResp> {

    private static Logger logger = LoggerFactory.getLogger(ClientCreateGroupHandler.class);



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResp msg) throws Exception {
        if (!msg.getSuccess()){
            logger.info(JSON.toJSONString(msg));
            return;
        }
        logger.info("群聊创建成功，群id为：{}; 群成员为：{}",msg.getGroupId(), msg.getUserIds());
    }


}
