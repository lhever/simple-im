package com.lhever.simpleim.common.command;

import com.lhever.simpleim.common.msg.MessageReq;
import com.lhever.simpleim.common.util.ChannelUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 广播 broadcast::msg
 */
public class BroadcastCmd implements ConsoleCmd {

    private static Logger logger = LoggerFactory.getLogger(P2PCmd.class);

    public void exec(Channel channel,String string) {
        String[] strs = string.split("::");
        if(strs.length < 2){
            logger.info("广播请按如下格式发送：broadcast::msg");
        }else {
            MessageReq packet = buildMessage(strs[1]);
            ChannelUtils.writeAndFlush(channel,packet);
        }
    }

    private MessageReq buildMessage(String msg) {
        MessageReq request = new MessageReq();
        request.setMessage(msg);
        return request;
    }
}
