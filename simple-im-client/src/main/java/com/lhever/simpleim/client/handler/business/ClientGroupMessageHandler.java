package com.lhever.simpleim.client.handler.business;

import com.lhever.common.core.utils.LogUtils;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.simpleim.common.msg.GroupMessageAck;
import com.lhever.simpleim.common.msg.GroupMessageResp;
import com.lhever.simpleim.common.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientGroupMessageHandler extends SimpleChannelInboundHandler<GroupMessageResp> {
    private static Logger logger = LoggerFactory.getLogger(ClientGroupMessageHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResp msg) throws Exception {
        String userId = LoginUtil.getUserId(ctx.channel());
        logger.info("用户:{}收到组:{}的消息. 发送人是:{}, groupMsgId:{}, userGroupMsgId:{}, content:{}", userId,
                msg.getGroupId(), msg.getSendId(), msg.getGroupMsgId(), msg.getUserGroupMsgId(), msg.getGroupMsg());
        if (!StringUtils.equals(userId, msg.getReceiveId())) {
            logger.error("用户:{}不是组消息的真正接受者");
        }

        GroupMessageAck ack = new GroupMessageAck();
        ack.setReceiveId(msg.getReceiveId());
        ack.setUserGroupMsgId(msg.getUserGroupMsgId());

        ack.setGroupId(msg.getGroupId());
        ack.setGroupMsgId(msg.getGroupMsgId());

        ctx.writeAndFlush(ack);

    }


}
