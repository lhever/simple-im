package com.lhever.simpleim.server.util;

import com.lhever.simpleim.common.msg.Msg;
import com.lhever.simpleim.common.util.SessionUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/28 23:38
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/28 23:38
 * @modify by reason:{方法名}:{原因}
 */
public class ServerSendUtils {

    private static Logger logger = LoggerFactory.getLogger(ServerSendUtils.class);


    public static boolean write2Channel(Msg response, Channel targetChannel) {
        //写数据
        if (targetChannel != null && SessionUtil.hasLogin(targetChannel)) {
            targetChannel.writeAndFlush(response);
            return true;
        } else {
            logger.info(" 该用户未登录，无法向他发送消息！");
        }
        return false;
    }


}
