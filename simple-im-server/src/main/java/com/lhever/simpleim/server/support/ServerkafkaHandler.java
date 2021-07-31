package com.lhever.simpleim.server.support;

import com.alibaba.fastjson.JSON;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.common.kafka.ack.KafkaAck;
import com.lhever.common.kafka.handler.MsgHandler;
import com.lhever.simpleim.common.consts.KafkaDataType;
import com.lhever.simpleim.common.dto.kafka.KafkaP2PMessage;
import com.lhever.simpleim.common.dto.kafka.KafkaSingleGroupMessage;
import com.lhever.simpleim.common.msg.GroupMessageResp;
import com.lhever.simpleim.common.msg.MessageResp;
import com.lhever.simpleim.common.util.JsonUtils;
import com.lhever.simpleim.common.util.SessionUtil;
import com.lhever.simpleim.server.util.ServerSendUtils;
import io.netty.channel.Channel;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/28 23:28
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/28 23:28
 * @modify by reason:{方法名}:{原因}
 */
public class ServerkafkaHandler implements MsgHandler<String, String> {

    private static Logger logger = LoggerFactory.getLogger(ServerkafkaHandler.class);

    @Override
    public void Handle(ConsumerRecord<String, String> record, KafkaAck ack) {
        String value = record.value();
        if (StringUtils.isBlank(value)) {
            return;
        }

        if (value.startsWith(KafkaDataType.P2P_MSG)) {
            handleP2PMsg(value);
        } else if (value.startsWith(KafkaDataType.GROUP_SINGLE_MSG)) {
            handleGroupSingleMsg(value);
        } else {
            logger.error("unknown kafka msg:{}", value);
        }
    }

    public void handleP2PMsg(String value) {
        String msg = value.substring(KafkaDataType.P2P_MSG.length());
        KafkaP2PMessage p2PMessage = JsonUtils.json2Obj(msg, KafkaP2PMessage.class);
        String receiveId = p2PMessage.getReceiveId();
        Channel targetChannel = SessionUtil.getChannelByUserId(receiveId);

        if (targetChannel != null) {
            MessageResp onlineResp = new MessageResp();
            onlineResp.setId(p2PMessage.getId());
            onlineResp.setSendId(p2PMessage.getSendId());
            onlineResp.setTargetId(p2PMessage.getReceiveId());
            onlineResp.setMessage(p2PMessage.getMessage());
            onlineResp.setCreateTime(p2PMessage.getCreateTime());
            ServerSendUtils.write2Channel(onlineResp, targetChannel);
            logger.info("发送消息给客户端:{}，内容是:{}", receiveId, JSON.toJSONString(p2PMessage));
        } else {
            logger.error("发送单聊消息给用户:{}失败", receiveId);
        }
    }

    public void handleGroupSingleMsg(String value) {
        String msg = value.substring(KafkaDataType.GROUP_SINGLE_MSG.length());
        KafkaSingleGroupMessage singleMsg = JsonUtils.json2Obj(msg, KafkaSingleGroupMessage.class);
        String receiveId = singleMsg.getReceiveId();
        Channel targetChannel = SessionUtil.getChannelByUserId(receiveId);

        if (targetChannel != null) {
            GroupMessageResp onlineResp = new GroupMessageResp();
            onlineResp.setGroupId(singleMsg.getGroupId());
            onlineResp.setSendId(singleMsg.getSendId());
            onlineResp.setGroupMsg(singleMsg.getGroupMsg());
            ServerSendUtils.write2Channel(onlineResp, targetChannel);
            logger.info("发送消息给客户端:{}，内容是:{}", receiveId, JSON.toJSONString(singleMsg));
        } else {
            logger.error("发送群消息给用户:{}失败", receiveId);
        }
    }
}
