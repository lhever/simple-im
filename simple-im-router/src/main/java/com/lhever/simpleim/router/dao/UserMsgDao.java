package com.lhever.simpleim.router.dao;


import com.lhever.simpleim.common.pojo.UserMsg;
import com.lhever.simpleim.router.basic.cfg.SessionFactoryHolder;
import com.lhever.simpleim.router.dao.mapper.usermsg.UserMsgMapper;
import com.lhever.simpleim.router.dao.mapper.usermsg.UserMsgMapperWrapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMsgDao {

    @Autowired
    private SessionFactoryHolder sessionFactoryHolder;


    public void save(UserMsg userMsg) {
        if (userMsg == null) {
            return;
        }
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sessionFactory = sessionFactoryHolder.getSessionFactory(userMsg.getReceiveId());
            sqlSession = sessionFactory.openSession();
            UserMsgMapper userMsgMapper = sqlSession.getMapper(UserMsgMapper.class);
            UserMsgMapperWrapper wrapper = new UserMsgMapperWrapper(userMsgMapper);
            wrapper.insert(userMsg);
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }















}
