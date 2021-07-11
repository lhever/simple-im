package com.lhever.simpleim.common.consts;

public interface MsgType {

    /**
     * 心跳包
     */
    final Integer HEART_BEAT = 0;

    /**
     * 登录请求
     */
    final Integer LOGIN_REQ = 1;

    /**
     * 登录响应
     */
    final Integer LOGIN_RESP = 2;

    /**
     * 消息请求
     */
    final Integer MESSAGE_REQUEST = 3;
    /**
     * 消息响应
     */
    final Integer MESSAGE_RESPONSE = 4;
    /**
     * 创建群聊
     */
    final Integer CREATE_GROUP_REQUEST = 5;

    /**
     * 创建群聊响应
     */
    final Integer CREATE_GROUP_RESPONSE = 6;

    /**
     * 发送群聊
     */
    final Integer GROUP_MESSAGE_REQUEST = 7;

    /**
     * 创建群聊响应
     */
    final Integer GROUP_MESSAGE_RESPONSE = 8;




}
