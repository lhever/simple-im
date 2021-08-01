package com.lhever.simpleim.common.util;

import com.lhever.simpleim.common.command.CmdManager;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;


public class Scan implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(Scan.class);

    private Channel channel;

    public Scan(Channel channel){
        this.channel = channel;
    }

    public void run() {
        Scanner sc = new Scanner(System.in,"utf-8");
        while (!Thread.interrupted()) {
            if(LoginUtil.hasLogin(channel)){
                String msg = sc.nextLine();
                if(!StringUtil.isNullOrEmpty(msg)  &&  !StringUtil.isNullOrEmpty(msg.trim())){
                    CmdManager.getInstance().exec(this.channel,msg);
                }
            }
        }

    }
//    public void run() {
//        Scanner sc = new Scanner(System.in);
//        while (!Thread.interrupted()) {
//            if(LoginUtil.hasLogin(channel)){
//                String msg = sc.nextLine();
//                MessageRequestPacket messageRequestPacket = buildMessageRequestPacket(msg);
//                if(messageRequestPacket != null) {
//                    writeMessage(messageRequestPacket);
//                }
//            }
//        }
//
//    }

//    private void writeMessage(MessageRequestPacket messageRequestPacket) {
//        ByteBuf buf = PacketCodeC.getInstance().encode(messageRequestPacket);
//        channel.writeAndFlush(buf);
//    }
//
//    private MessageRequestPacket buildMessageRequestPacket(String msg) {
//        MessageRequestPacket request = new MessageRequestPacket();
//        //发送消息格式为   userId::message ，如果userId为空，则发给所有人
//        String[] strs = msg.split("::");
//        if(strs.length < 2){
//            logger.info("发送广播：{}",msg);
//            request.setMessage(msg);
//        }else {
//            logger.info("发送消息给{}：{}",strs[0],strs[1]);
//            request.setToUserId(strs[0]);
//            request.setMessage(strs[1]);
//        }
//        return request;
//    }


}
