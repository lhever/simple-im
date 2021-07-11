package com.lhever.simpleim.common.Command;

import com.lhever.simpleim.common.msg.CreateGroupReq;
import com.lhever.simpleim.common.util.ChannelUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunnick on 2019/1/20/020.
 * 发起群聊 createGroup::userId1,userId2,userId3...
 */
public class CreateGroupCommand implements ConsoleCommand {

    private static Logger logger = LoggerFactory.getLogger(CreateGroupCommand.class);

    @Override
    public void exec(Channel channel, String string) {

        String[] strs = string.split("::");
        if(strs.length < 2){
            logger.info("创建群聊请使用 createGroup::userId1,userId2,userId3... 命令！");
            return;
        }
        CreateGroupReq packet = buildPacket(strs[1]);
        ChannelUtils.writeAndFlush(channel,packet);
    }

    private CreateGroupReq buildPacket(String usersString) {
        CreateGroupReq packet = new CreateGroupReq();
        String[] users = usersString.split(",");
        List<String> userList = new ArrayList<String>();
        for (String str : users){
            userList.add(str);
        }
        packet.setUserIds(userList);

        return packet;
    }
}
