package com.lhever.simpleim.server.support;

import com.alibaba.fastjson.JSON;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.common.kafka.ack.KafkaAck;
import com.lhever.common.kafka.handler.MsgHandler;
import com.lhever.simpleim.common.consts.KafkaDataType;
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
public class kafkaMsgHandler implements MsgHandler<String, String> {

    private static Logger logger = LoggerFactory.getLogger(kafkaMsgHandler.class);

    @Override
    public void Handle(ConsumerRecord<String, String> record, KafkaAck ack) {
        String value = record.value();
        if (StringUtils.isBlank(value)) {
            return;
        }

        if (value.startsWith(KafkaDataType.SINGLE_CHAT)) {
            String substring = value.substring(KafkaDataType.SINGLE_CHAT.length());
            MessageResp messageResp = JsonUtils.json2Obj(substring, MessageResp.class);
            String targetId = messageResp.getTargetId();
            Channel targetChannel = SessionUtil.getChannelByUserId(targetId);

            if (targetChannel != null) {
                ServerSendUtils.write2Channel(messageResp, targetChannel);
                logger.info("发送消息给客户端{}，内容是:{}", targetId, JSON.toJSONString(messageResp));
            } else {
                ServerSendUtils.write2RouterByKafka(targetId, messageResp);
            }
        }

    }
}
