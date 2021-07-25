package com.lhever.simpleim.server.handler.business;

import com.lhever.simpleim.common.msg.GroupMessageReq;
import com.lhever.simpleim.common.msg.GroupMessageResp;
import com.lhever.simpleim.common.util.GroupUtils;
import com.lhever.simpleim.common.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServerGroupMessageHandler extends SimpleChannelInboundHandler<GroupMessageReq> {



    private static Logger logger = LoggerFactory.getLogger(ServerGroupMessageHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageReq msg) throws Exception {

        ChannelGroup channelGroup = GroupUtils.getChannelGroup(msg.getGroupId());
        if(channelGroup == null){
            logger.info("群聊不存在!");
            return;
        }
        GroupMessageResp response = new GroupMessageResp();
        response.setGroupMsg(msg.getGroupMsg());
        response.setGroupId(msg.getGroupId());
        response.setFromUserName(SessionUtil.getSessionByChannel(ctx.channel()).getUserName());
        for(Channel channel : channelGroup){
            channel.writeAndFlush(response);
        }

    }
}
