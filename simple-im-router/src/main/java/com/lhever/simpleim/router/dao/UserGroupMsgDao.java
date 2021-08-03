package com.lhever.simpleim.router.dao;


import com.lhever.simpleim.common.pojo.UserGroup;
import com.lhever.simpleim.common.pojo.UserGroupMsg;
import com.lhever.simpleim.common.pojo.UserMsg;
import com.lhever.simpleim.router.basic.cfg.SessionFactoryHolder;
import com.lhever.simpleim.router.dao.mapper.usergroupmsg.UserGroupMsgMapper;
import com.lhever.simpleim.router.dao.mapper.usergroupmsg.UserGroupMsgMapperWrapper;
import com.lhever.simpleim.router.dao.mapper.usermsg.UserMsgMapper;
import com.lhever.simpleim.router.dao.mapper.usermsg.UserMsgMapperWrapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserGroupMsgDao {

    @Autowired
    private SessionFactoryHolder sessionFactoryHolder;


    public void save(UserGroupMsg userGroupMsg) {
        if (userGroupMsg == null) {
            return;
        }
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sessionFactory = sessionFactoryHolder.getSessionFactory(userGroupMsg.getReceiveId());
            sqlSession = sessionFactory.openSession();
            UserGroupMsgMapper userGroupMsgMapper = sqlSession.getMapper(UserGroupMsgMapper.class);
            UserGroupMsgMapperWrapper wrapper = new UserGroupMsgMapperWrapper(userGroupMsgMapper);
            wrapper.insert(userGroupMsg);
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }




    public void updateStatus(String receiveId, String msgId, Integer status) {
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sessionFactory = sessionFactoryHolder.getSessionFactory(receiveId);
            sqlSession = sessionFactory.openSession();
            UserGroupMsgMapper userGroupMsgMapper = sqlSession.getMapper(UserGroupMsgMapper.class);
            UserGroupMsgMapperWrapper wrapper = new UserGroupMsgMapperWrapper(userGroupMsgMapper);
            wrapper.updateStatusById(msgId, status);
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }















}
