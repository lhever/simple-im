package com.lhever.simpleim.router.service;

import com.lhever.common.core.utils.CollectionUtils;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.simpleim.common.consts.ContentType;
import com.lhever.simpleim.common.consts.ImConsts;
import com.lhever.simpleim.common.dto.kafka.*;
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
import java.util.List;

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
        userMsg.setReadStatus(ImConsts.UN_READ);

        userMsgDao.save(userMsg);
    }

    public void ackMessage(KafkaMessageAck ack) {
        userMsgDao.updateStatus(ack.getReceiveId(), ack.getMsgId(), ImConsts.READED);
    }


    public void ackMessage(KafkaGroupMessageAck ack) {
        userGroupMsgDao.updateStatus(ack.getReceiveId(), ack.getUserGroupMsgId(), ImConsts.READED);

        groupMsgDao.incrReadCount(ack.getGroupId(), ack.getGroupMsgId());
    }








    public GroupMsg saveGroupMsg(KafkaBatchGroupMessage groupBatchMsg) {
        String groupId = groupBatchMsg.getGroupId();
        Group group = groupDao.findGroup(groupId);
        if (group == null) {
            return null;
        }

        String memberIds = group.getMemberIds();
        List<String> memberList = StringUtils.splitToList(memberIds, ",");
        memberList = CollectionUtils.getNotBlank(memberList);
        //成员包括发送人自己
        memberList.add(groupBatchMsg.getSendId());

        GroupMsg groupMsg = new GroupMsg();
        groupMsg.setId(StringUtils.getUuid());
        groupMsg.setCreateId(groupBatchMsg.getSendId());
        groupMsg.setGroupId(groupBatchMsg.getGroupId());
        groupMsg.setReceiveIds(StringUtils.join(memberList, ","));
        groupMsg.setType(0);
        groupMsg.setContent(groupBatchMsg.getGroupMsg());
        groupMsg.setReadStatus(ImConsts.PARTIAL_READED);
        //对于群消息，发送人自己肯定已经阅读了，所以已读数是 1
        groupMsg.setReadCount(1);
        Date now = new Date();
        groupMsg.setUpdateTime(now);
        groupMsg.setCreateTime(now);
        groupMsgDao.save(groupMsg);
        return groupMsg;
    }


    public UserGroupMsg saveUserGroupMsg(KafkaSingleGroupMessage groupSingleMsg ) {
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
        boolean self = StringUtils.equals(groupSingleMsg.getSendId(), groupSingleMsg.getReceiveId());
        if (self) {
            //自己发出去的消息，肯定是已读
            userMsg.setStatus(ImConsts.READED);
        } else {
            userMsg.setStatus(ImConsts.UN_READ);
        }
        userGroupMsgDao.save(userMsg);
        return userMsg;
    }


}
