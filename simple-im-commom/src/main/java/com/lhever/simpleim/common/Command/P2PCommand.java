package com.lhever.simpleim.common.Command;

import com.lhever.simpleim.common.msg.MessageReq;
import com.lhever.simpleim.common.util.ChannelUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 私聊 sendToUser::userId::msg
 */
public class P2PCommand implements ConsoleCommand{
    private static Logger logger = LoggerFactory.getLogger(P2PCommand.class);

    public void exec(Channel channel,String string) {
        String[] strs = string.split("::");
        if(strs.length < 3){
            logger.info("私聊请按如下格式发送：sendToUser::userId::msg");
        }else {
            MessageReq packet = buildMessageRequestPacket(strs[1],strs[2]);
            ChannelUtils.writeAndFlush(channel,packet);
        }
    }


    private MessageReq buildMessageRequestPacket(String userId, String msg) {
        MessageReq request = new MessageReq();
            logger.info("发送消息给{}：{}",userId,msg);
            request.setTargetUserId(userId);
            request.setMessage(msg);
        return request;
    }
}
