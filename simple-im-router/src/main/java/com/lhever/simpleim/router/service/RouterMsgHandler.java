package com.lhever.simpleim.router.service;

import com.lhever.common.core.utils.JsonUtils;
import com.lhever.common.core.utils.ParseUtils;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.common.kafka.ack.KafkaAck;
import com.lhever.common.kafka.handler.MsgHandler;
import com.lhever.simpleim.common.consts.ImConsts;
import com.lhever.simpleim.common.consts.KafkaDataType;
import com.lhever.simpleim.common.dto.kafka.*;
import com.lhever.simpleim.common.pojo.GroupMsg;
import com.lhever.simpleim.common.pojo.UserGroupMsg;
import com.lhever.simpleim.common.util.KafkaUtils;
import com.lhever.simpleim.common.util.RedisUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/28 21:06
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/28 21:06
 * @modify by reason:{方法名}:{原因}
 */
@Component("routerMsgHandler")
public class RouterMsgHandler implements MsgHandler<String, String> {

    private static Logger logger = LoggerFactory.getLogger(RouterMsgHandler.class);

    @Autowired
    private MessageService messageService;

    public void Handle(ConsumerRecord<String, String> record, KafkaAck ack) {
        try {
            doHandle(record, ack);
        } finally {
            ack.acknowledge();
        }
    }


    public void doHandle(ConsumerRecord<String, String> record, KafkaAck ack) {
        String value = record.value();
        if (StringUtils.isBlank(value)) {
            return;
        }

        if (value.startsWith(KafkaDataType.P2P_MSG)) {
            handleP2P(value.substring(KafkaDataType.P2P_MSG.length()));
            return;
        }

        if (value.startsWith(KafkaDataType.GROUP_BATCH_MSG)) {
            handleGroupBatch(value.substring(KafkaDataType.GROUP_BATCH_MSG.length()));
            return;
        }

        if (value.startsWith(KafkaDataType.P2P_MSG_ACK)) {
            handleP2PAck(value.substring(KafkaDataType.P2P_MSG_ACK.length()));
            return;
        }

        if (value.startsWith(KafkaDataType.GROUP_SINGLE_MSG_ACK)) {
            ackGroupSingle(value.substring(KafkaDataType.GROUP_SINGLE_MSG_ACK.length()));
            return;
        }
    }


    public void handleP2P(String msg) {
        KafkaP2PMessage p2PMessage = JsonUtils.json2Object(msg, KafkaP2PMessage.class);
        if (p2PMessage.getSaveOnly()) {
            logger.info("消息:{}, 发送人:{}, 接收人:{}已经发送，仅保存到数据库",
                    p2PMessage.getId(), p2PMessage.getSendId(), p2PMessage.getReceiveId());
            messageService.saveUserMsg(p2PMessage);
        } else {
            messageService.saveUserMsg(p2PMessage);
            String receiveId = p2PMessage.getReceiveId();
            String value = RedisUtils.get(ImConsts.LOGIN_KEY + receiveId);
            //说明用户不在线
            if (StringUtils.isNotBlank(value)) {

                String replace = value.replace(":", "-");
                String topicPrefix = ParseUtils.parseArgs(KafkaUtils.SERVER_TOPIC_TPL, replace);
                KafkaUtils.sendToServer(Objects.hash(receiveId), topicPrefix, KafkaDataType.P2P_MSG, p2PMessage);

            }
        }
    }


    public void handleP2PAck(String msg) {
        KafkaMessageAck messageAck = JsonUtils.json2Object(msg, KafkaMessageAck.class);
        if (messageAck == null) {
            return;
        }
        messageService.ackMessage(messageAck);
    }

    public void ackGroupSingle(String msg) {
        KafkaGroupSingleMessageAck messageAck = JsonUtils.json2Object(msg, KafkaGroupSingleMessageAck.class);
        if (messageAck == null) {
            return;
        }
        messageService.ackMessage(messageAck);
    }


    public void handleGroupBatch(String msg) {
        KafkaBatchGroupMessage groupBatchMsg = JsonUtils.json2Object(msg, KafkaBatchGroupMessage.class);
        GroupMsg groupMsg = messageService.saveGroupMsg(groupBatchMsg);
        if (groupMsg == null) {
            return;
        }
        String[] split = groupMsg.getReceiveIds().split(",");
        for (String memberId : split) {
            KafkaSingleGroupMessage groupSingleMsg = new KafkaSingleGroupMessage();
            groupSingleMsg.setGroupMsgId(groupMsg.getId());
            groupSingleMsg.setSendId(groupBatchMsg.getSendId());
            groupSingleMsg.setGroupId(groupMsg.getGroupId());
            groupSingleMsg.setReceiveId(memberId);
            groupSingleMsg.setGroupMsg(groupBatchMsg.getGroupMsg());

            //将群组消息 group_msg 冗余到 user_group_msg 表
            UserGroupMsg userGroupMsg = messageService.saveUserGroupMsg(groupSingleMsg);
            groupSingleMsg.setUserGroupMsgId(userGroupMsg.getId());

            boolean self = StringUtils.equals(memberId, groupMsg.getCreateId());
            //如果用户在线, 另外，自己不用发给自己
            if (!self) {
                String value = RedisUtils.get(ImConsts.LOGIN_KEY + memberId);
                if (StringUtils.isNotBlank(value)) {
                    String replace = value.replace(":", "-");
                    String topicPrefix = ParseUtils.parseArgs(KafkaUtils.SERVER_TOPIC_TPL, replace);
                    KafkaUtils.sendToServer(Objects.hash(memberId), topicPrefix, KafkaDataType.GROUP_SINGLE_MSG, groupSingleMsg);
                }
            }
        }
    }


}
