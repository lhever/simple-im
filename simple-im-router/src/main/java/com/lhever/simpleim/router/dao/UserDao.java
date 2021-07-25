package com.lhever.simpleim.router.dao;

import com.lhever.simpleim.router.dao.mapper.UserMapper;
import com.lhever.simpleim.router.pojo.User;
import com.lhever.simpleim.router.pojo.UserExample;

import java.util.List;

public class UserDao implements UserMapper {

    @Override
    public long countByExample(UserExample example) {
        return 0;
    }

    @Override
    public int deleteByExample(UserExample example) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return 0;
    }

    @Override
    public int insert(User record) {
        return 0;
    }

    @Override
    public int insertSelective(User record) {
        return 0;
    }

    @Override
    public List<User> selectByExample(UserExample example) {
        return null;
    }

    @Override
    public User selectByPrimaryKey(String id) {
        return null;
    }

    @Override
    public int updateByExampleSelective(User record, UserExample example) {
        return 0;
    }

    @Override
    public int updateByExample(User record, UserExample example) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(User record) {
        return 0;
    }
}
