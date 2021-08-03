package com.lhever.simpleim.router.dao;


import com.lhever.simpleim.common.pojo.GroupMsg;
import com.lhever.simpleim.common.pojo.UserMsg;
import com.lhever.simpleim.router.basic.cfg.SessionFactoryHolder;
import com.lhever.simpleim.router.dao.mapper.group.GroupMapper;
import com.lhever.simpleim.router.dao.mapper.group.GroupMapperWrapper;
import com.lhever.simpleim.router.dao.mapper.groupmsg.GroupMsgMapper;
import com.lhever.simpleim.router.dao.mapper.groupmsg.GroupMsgMapperWrapper;
import com.lhever.simpleim.router.dao.mapper.usermsg.UserMsgMapper;
import com.lhever.simpleim.router.dao.mapper.usermsg.UserMsgMapperWrapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupMsgDao {

    @Autowired
    private SessionFactoryHolder sessionFactoryHolder;


    public void save(GroupMsg groupMsg) {
        if (groupMsg == null) {
            return;
        }
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sessionFactory = sessionFactoryHolder.getSessionFactory(groupMsg.getGroupId());
            sqlSession = sessionFactory.openSession();
            GroupMsgMapper groupMsgMapper = sqlSession.getMapper(GroupMsgMapper.class);
            GroupMsgMapperWrapper wrapper = new GroupMsgMapperWrapper(groupMsgMapper);
            wrapper.insert(groupMsg);
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }



    public int incrReadCount(String groupId, String groupMsgId) {
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sessionFactory = sessionFactoryHolder.getSessionFactory(groupId);
            sqlSession = sessionFactory.openSession();
            GroupMsgMapper groupMsgMapper = sqlSession.getMapper(GroupMsgMapper.class);
            GroupMsgMapperWrapper wrapper = new GroupMsgMapperWrapper(groupMsgMapper);
           return wrapper.incrReadCount(groupMsgId);
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }















}
