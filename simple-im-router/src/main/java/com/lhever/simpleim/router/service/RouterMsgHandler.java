package com.lhever.simpleim.router.service;

import com.lhever.common.core.utils.JsonUtils;
import com.lhever.common.core.utils.ParseUtils;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.common.kafka.ack.KafkaAck;
import com.lhever.common.kafka.handler.MsgHandler;
import com.lhever.simpleim.common.consts.ImConsts;
import com.lhever.simpleim.common.consts.KafkaDataType;
import com.lhever.simpleim.common.msg.MessageResp;
import com.lhever.simpleim.common.util.KafkaUtils;
import com.lhever.simpleim.common.util.RedisUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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

    @Autowired
    private UserMessageService userMessageService;

    @Override

    public void Handle(ConsumerRecord<String, String> record, KafkaAck ack) {
        String value = record.value();
        if (StringUtils.isBlank(value)) {
            return;
        }

        if (value.startsWith(KafkaDataType.SINGLE_CHAT)) {
            handleMsg(value.substring(KafkaDataType.SINGLE_CHAT.length()));
        }
    }


    public void handleMsg(String msg) {
        MessageResp messageResp = JsonUtils.json2Object(msg, MessageResp.class);
        String targetId = messageResp.getTargetId();

        String value = RedisUtils.get(ImConsts.LOGIN_KEY + targetId);

        //说明用户不在线
        if (StringUtils.isBlank(value)) {
            userMessageService.saveMessage(messageResp);
        } else {
            String replace = value.replace(":", "-");
            String topic = ParseUtils.parseArgs(KafkaUtils.SERVER_TOPIC_TPL, replace);
            KafkaUtils.sendToServer(Objects.hash(messageResp.getTargetId()), topic, KafkaDataType.SINGLE_CHAT, messageResp);
        }
    }


}
