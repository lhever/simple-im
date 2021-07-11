package com.lhever.simpleim.common.Command;

import com.lhever.simpleim.common.msg.GroupMessageReq;
import com.lhever.simpleim.common.util.ChannelUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 群聊 sendToGroup::groupId::msg
 */
public class GroupMessageCommand implements ConsoleCommand {

    private static Logger logger = LoggerFactory.getLogger(GroupMessageCommand.class);

    @Override
    public void exec(Channel channel, String string) {
        String[] strs = string.split("::");
        if(strs.length < 3){
            logger.info("群聊请输入如下命令 sendToGroup::groupId::msg");
            return;
        }
        GroupMessageReq packet = BuildRequestPacket(strs[1],strs[2]);
        ChannelUtils.writeAndFlush(channel,packet);
    }

    private GroupMessageReq BuildRequestPacket(String groupId, String groupMsg) {
        GroupMessageReq packet = new GroupMessageReq();
        packet.setGroupId(groupId);
        packet.setGroupMsg(groupMsg);
        return packet;
    }
}
