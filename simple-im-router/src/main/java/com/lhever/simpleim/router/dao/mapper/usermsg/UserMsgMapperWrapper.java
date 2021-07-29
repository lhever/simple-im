package com.lhever.simpleim.router.dao.mapper.usermsg;

import com.lhever.simpleim.common.pojo.UserMsg;
import com.lhever.simpleim.common.pojo.UserMsgExample;

import java.util.List;

public class UserMsgMapperWrapper implements UserMsgMapper {

    private UserMsgMapper userMsgMapper;

    public UserMsgMapperWrapper(UserMsgMapper userMapper) {
        this.userMsgMapper = userMapper;
    }

    @Override
    public long countByExample(UserMsgExample example) {
        return userMsgMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(UserMsgExample example) {
        return userMsgMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return userMsgMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserMsg record) {
        return userMsgMapper.insert(record);
    }

    @Override
    public int insertSelective(UserMsg record) {
        return userMsgMapper.insertSelective(record);
    }

    @Override
    public List<UserMsg> selectByExample(UserMsgExample example) {
        return userMsgMapper.selectByExample(example);
    }

    @Override
    public UserMsg selectByPrimaryKey(String id) {
        return userMsgMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(UserMsg record, UserMsgExample example) {
        return userMsgMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(UserMsg record, UserMsgExample example) {
        return userMsgMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(UserMsg record) {
        return userMsgMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserMsg record) {
        return userMsgMapper.updateByPrimaryKey(record);
    }


}
