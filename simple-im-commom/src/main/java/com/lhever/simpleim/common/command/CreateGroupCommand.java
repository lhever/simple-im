package com.lhever.simpleim.common.command;

import com.lhever.simpleim.common.msg.CreateGroupReq;
import com.lhever.simpleim.common.util.ChannelUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 发起群聊 createGroup::userId1,userId2,userId3...
 */
public class CreateGroupCommand implements ConsoleCommand {

    private static Logger logger = LoggerFactory.getLogger(CreateGroupCommand.class);

    @Override
    public void exec(Channel channel, String string) {

        String[] strs = string.split("::");
        if (strs.length < 3) {
            logger.info("创建群聊请使用 createGroup::groupName::userId1,userId2,userId3... 命令！");
            return;
        }
        CreateGroupReq packet = buildPacket(strs[1], strs[2]);
        ChannelUtils.writeAndFlush(channel, packet);
    }

    private CreateGroupReq buildPacket(String groupName, String usersString) {
        CreateGroupReq packet = new CreateGroupReq();
        String[] users = usersString.split(",");
        List<String> userList = new ArrayList<String>();
        for (String str : users) {
            userList.add(str);
        }
        packet.setGroupName(groupName);
        packet.setUserIds(userList);

        return packet;
    }
}
