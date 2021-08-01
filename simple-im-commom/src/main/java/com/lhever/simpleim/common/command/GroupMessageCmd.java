package com.lhever.simpleim.common.command;

import com.lhever.simpleim.common.msg.GroupMessageReq;
import com.lhever.simpleim.common.util.ChannelUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 群聊 sendToGroup::groupId::msg
 */
public class GroupMessageCmd implements ConsoleCmd {

    private static Logger logger = LoggerFactory.getLogger(GroupMessageCmd.class);

    @Override
    public void exec(Channel channel, String string) {
        String[] strs = string.split("::");
        if(strs.length < 3){
            logger.info("群聊请输入如下命令 sendToGroup::groupId::msg");
            return;
        }
        GroupMessageReq groupMsg = buildGroupMessage(strs[1],strs[2]);
        ChannelUtils.writeAndFlush(channel,groupMsg);
    }

    private GroupMessageReq buildGroupMessage(String groupId, String groupMsg) {
        GroupMessageReq packet = new GroupMessageReq();
        packet.setGroupId(groupId);
        packet.setGroupMsg(groupMsg);
        return packet;
    }
}
