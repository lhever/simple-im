package com.lhever.simpleim.router.service;

import com.lhever.common.core.utils.StringUtils;
import com.lhever.simpleim.common.consts.ContentType;
import com.lhever.simpleim.common.msg.MessageResp;
import com.lhever.simpleim.common.pojo.UserMsg;
import com.lhever.simpleim.router.dao.UserMsgDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserMessageService {

    @Autowired
    private UserMsgDao userMsgDao;


    public void saveMessage(MessageResp messageResp) {
        UserMsg userMsg = new UserMsg();
        userMsg.setId(StringUtils.getUuid());
        userMsg.setCreateId(messageResp.getSendId());
        userMsg.setReceiveId(messageResp.getTargetId());
        userMsg.setContent(messageResp.getMessage());
        userMsg.setType(ContentType.MSG_TEXT);
        Date now = new Date();
        userMsg.setCreateTime(now);
        userMsg.setUpdateTime(now);
        userMsg.setSendStatus(0);
        userMsg.setReadStatus(0);

        userMsgDao.save(userMsg);
    }


}
