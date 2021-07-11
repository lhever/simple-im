package com.lhever.simpleim.server.business;

import com.alibaba.fastjson.JSON;
import com.lhever.simpleim.common.msg.CreateGroupReq;
import com.lhever.simpleim.common.msg.CreateGroupResp;
import com.lhever.simpleim.common.util.GroupUtils;
import com.lhever.simpleim.common.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class ServerCreateGroupHandler extends SimpleChannelInboundHandler<CreateGroupReq> {

    private static Logger logger = LoggerFactory.getLogger(ServerCreateGroupHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupReq msg) throws Exception {

        CreateGroupResp response = createGroup(ctx,msg);
        logger.info("返回给客户端:{}",JSON.toJSONString(response));
        for (String userId : msg.getUserIds()){
            SessionUtil.getChannelByUserId(userId).writeAndFlush(response);
        }

    }

    private CreateGroupResp createGroup(ChannelHandlerContext ctx, CreateGroupReq packet) {
        CreateGroupResp response = new CreateGroupResp();
        logger.info("收到创建群聊请求：{}", JSON.toJSONString(packet));

        List<String> users = packet.getUserIds();
        String groupId = GroupUtils.createGroup(ctx,users);

        if(StringUtil.isNullOrEmpty(groupId)){
           response.setSuccess(false);
        }
        List<String> userNames = getUserNames(users);

        response.setGroupId(groupId);
        response.setUserIds(userNames);

        return response;
    }

    private List<String> getUserNames(List<String> users) {
        List<String> userNames = new ArrayList<>();

        for(String id : users){
            Channel channel = SessionUtil.getChannelByUserId(id);
            if (channel != null){
                String name = SessionUtil.getSessionByChannel(channel).getUserName();
                userNames.add(name);
            }
        }
        return userNames;
    }
}
