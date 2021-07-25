package com.lhever.simpleim.router.dao;


import com.lhever.simpleim.common.pojo.User;
import com.lhever.simpleim.router.basic.cfg.SessionFactoryHolder;
import com.lhever.simpleim.router.dao.mapper.UserMapper;
import com.lhever.simpleim.router.dao.mapper.UserMapperWrapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDao {

    @Autowired
    private SessionFactoryHolder sessionFactoryHolder;


    public User findUser(String userName, String pwd) {
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory aDefault = sessionFactoryHolder.getDefault();
            sqlSession = aDefault.openSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            UserMapperWrapper wrapper = new UserMapperWrapper(userMapper);
            return  wrapper.findBy(userName, pwd);
        } finally {
            sqlSession.commit();
            sqlSession.close();

        }
    }















}
