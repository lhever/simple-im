package com.lhever.simpleim.common.command;

import com.lhever.simpleim.common.msg.MessageReq;
import com.lhever.simpleim.common.msg.loginReq;
import com.lhever.simpleim.common.util.ChannelUtils;
import com.lhever.simpleim.common.util.LoginUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 私聊 sendToUser::userId::msg
 */
public class LoginCmd implements ConsoleCmd {

    private static Logger logger = LoggerFactory.getLogger(LoginCmd.class);

    public void exec(Channel channel, String string) {
        String[] strs = string.split("::");
        if (strs.length < 3) {
            logger.info("登陆请按如下格式发送：login::userName::passWord");
        } else {
            loginReq loginReq = buildAuthReq(strs[1], strs[2]);
            ChannelUtils.writeAndFlush(channel, loginReq);
        }
    }


    private loginReq buildAuthReq(String userName, String passWord) {
        loginReq req = new loginReq(userName, passWord);
        return req;
    }
}
