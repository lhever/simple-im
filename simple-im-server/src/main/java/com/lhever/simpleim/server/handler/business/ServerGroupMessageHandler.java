package com.lhever.simpleim.server.handler.business;

import com.lhever.simpleim.common.consts.KafkaDataType;
import com.lhever.simpleim.common.dto.kafka.KafkaBatchGroupMessage;
import com.lhever.simpleim.common.msg.GroupMessageReq;
import com.lhever.simpleim.common.msg.GroupMessageResp;
import com.lhever.simpleim.common.util.GroupUtils;
import com.lhever.simpleim.common.util.KafkaUtils;
import com.lhever.simpleim.common.util.SessionUtil;
import com.lhever.simpleim.server.util.ServerSendUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class ServerGroupMessageHandler extends SimpleChannelInboundHandler<GroupMessageReq> {



    private static Logger logger = LoggerFactory.getLogger(ServerGroupMessageHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageReq msg) throws Exception {

        String sendId = SessionUtil.getUserIdByChannel(ctx.channel());
        KafkaBatchGroupMessage batchGroupMessage = new KafkaBatchGroupMessage();
        batchGroupMessage.setSendId(sendId);
        batchGroupMessage.setGroupId(msg.getGroupId());
        batchGroupMessage.setGroupMsg(msg.getGroupMsg());

        KafkaUtils.sendToRouter(Objects.hash(sendId), KafkaDataType.GROUP_BATCH_MSG, batchGroupMessage);

    }
}
