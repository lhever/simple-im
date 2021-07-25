package com.lhever.simpleim.router.service.impl;

import com.lhever.common.core.utils.StringUtils;
import com.lhever.simpleim.common.consts.ContentType;
import com.lhever.simpleim.common.pojo.UserMsg;
import com.lhever.simpleim.router.service.UserMessageService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserMessageServiceImpl implements UserMessageService {



  /*  public void userMessage() {
        UserMsg userMsg = new UserMsg();
        userMsg.setId(StringUtils.getUuid());
        userMsg.setCreateId(sendId);
        userMsg.setReceiveId(msg.getTargetId());
        userMsg.setContent(msg.getMessage());
        userMsg.setType(ContentType.MSG_TEXT);
        Date now = new Date();
        userMsg.setCreateTime(now);
        userMsg.setUpdateTime(now);
        userMsg.setSendStatus(0);
        userMsg.setReadStatus(0);
    }*/
}
