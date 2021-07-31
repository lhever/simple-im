package com.lhever.simpleim.router.service;

import com.lhever.common.core.utils.StringUtils;
import com.lhever.simpleim.common.consts.ContentType;
import com.lhever.simpleim.common.dto.kafka.KafkaBatchGroupMessage;
import com.lhever.simpleim.common.dto.kafka.KafkaP2PMessage;
import com.lhever.simpleim.common.dto.kafka.KafkaSingleGroupMessage;
import com.lhever.simpleim.common.pojo.Group;
import com.lhever.simpleim.common.pojo.GroupMsg;
import com.lhever.simpleim.common.pojo.UserGroupMsg;
import com.lhever.simpleim.common.pojo.UserMsg;
import com.lhever.simpleim.router.dao.GroupDao;
import com.lhever.simpleim.router.dao.GroupMsgDao;
import com.lhever.simpleim.router.dao.UserGroupMsgDao;
import com.lhever.simpleim.router.dao.UserMsgDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageService {

    @Autowired
    private UserMsgDao userMsgDao;

    @Autowired
    private UserGroupMsgDao userGroupMsgDao;

    @Autowired
    private GroupMsgDao groupMsgDao;

    @Autowired
    private GroupDao groupDao;


    public void saveUserMsg(KafkaP2PMessage p2pMsg) {
        UserMsg userMsg = new UserMsg();
        userMsg.setId(StringUtils.getUuid());
        userMsg.setCreateId(p2pMsg.getSendId());
        userMsg.setReceiveId(p2pMsg.getReceiveId());
        userMsg.setContent(p2pMsg.getMessage());
        userMsg.setType(ContentType.MSG_TEXT);
        Date now = new Date();
        userMsg.setCreateTime(now);
        userMsg.setUpdateTime(now);
        userMsg.setSendStatus(0);
        userMsg.setReadStatus(0);

        userMsgDao.save(userMsg);
    }








    public GroupMsg saveGroupMsg(KafkaBatchGroupMessage groupBatchMsg) {
        String groupId = groupBatchMsg.getGroupId();
        Group group = groupDao.findGroup(groupId);
        if (group == null) {
            return null;
        }

        String memberIds = group.getMemberIds();

        GroupMsg groupMsg = new GroupMsg();
        groupMsg.setId(StringUtils.getUuid());
        groupMsg.setCreateId(groupBatchMsg.getSendId());
        groupMsg.setGroupId(groupBatchMsg.getGroupId());
        groupMsg.setReceiveIds(memberIds);
        groupMsg.setType(0);
        groupMsg.setContent(groupBatchMsg.getGroupMsg());
        groupMsg.setReadStatus(0);
        groupMsg.setSendStatus(0);
        Date now = new Date();
        groupMsg.setUpdateTime(now);
        groupMsg.setCreateTime(now);
        groupMsgDao.save(groupMsg);
        return groupMsg;
    }


    public void saveUserGroupMsg(KafkaSingleGroupMessage groupSingleMsg ) {
        UserGroupMsg userMsg = new UserGroupMsg();
        userMsg.setId(StringUtils.getUuid());
        userMsg.setCreateId(groupSingleMsg.getSendId());
        userMsg.setGroupId(groupSingleMsg.getGroupId());
        userMsg.setReceiveId(groupSingleMsg.getReceiveId());
        userMsg.setContent(groupSingleMsg.getGroupMsg());
        userMsg.setType(ContentType.MSG_TEXT);
        Date now = new Date();
        userMsg.setCreateTime(now);
        userMsg.setUpdateTime(now);
        userMsg.setStatus(0);
        userGroupMsgDao.save(userMsg);
    }


}
