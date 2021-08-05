package com.lhever.simpleim.common.command;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 命令管理器
 */
public class CmdManager {

    private static Logger logger = LoggerFactory.getLogger(CmdManager.class);

    /**
     * 单例模式
     */
    private static CmdManager instance = new CmdManager();

    private static Map<String, ConsoleCmd> commandMap = new ConcurrentHashMap<String, ConsoleCmd>();

    static {
        commandMap.putIfAbsent("login",new LoginCmd());
        commandMap.putIfAbsent("sendToUser",new P2PCmd());
        commandMap.putIfAbsent("broadcast",new BroadcastCmd());
        commandMap.putIfAbsent("createGroup",new CreateGroupCmd());
        commandMap.putIfAbsent("sendToGroup",new GroupMessageCmd());

    }

    public static CmdManager getInstance(){
        return instance;
    }

    private CmdManager(){  }


    /**
     * 命令执行：
     * 单聊 sendToUser::userId::msg
     * 群聊 sendToGroup::groupId::msg
     * 发起群聊 createGroup::userId1,userId2,userId3...
     * 退出群聊 quitGroup::groupId
     * 加入群聊 joinGroup::groupId
     * 查询所有在线用户 getAllUsers
     * 查询群聊中在线用户 getGroupUsers::groupId
     * 广播 broadcast::msg
     */
    public static void exec(Channel channel,String msg){
        String[] strs = msg.split("::");
        ConsoleCmd command = commandMap.get(strs[0]);
        if(command != null){
            command.exec(channel,msg);
        }else{
            logger.info("命令输入有误，请使用 command::content 的格式！");
        }
    }
}
