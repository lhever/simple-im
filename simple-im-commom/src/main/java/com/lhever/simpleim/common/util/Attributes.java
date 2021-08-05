package com.lhever.simpleim.common.util;

import io.netty.util.AttributeKey;


public interface Attributes {
    /**
     * 登录状态，使用channel.attr()方法保存
     */
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
    AttributeKey<String> USER_ID = AttributeKey.newInstance("userId");

    AttributeKey<Long> HAND_SHAKE_TIME = AttributeKey.newInstance("handShakeTime");
}
