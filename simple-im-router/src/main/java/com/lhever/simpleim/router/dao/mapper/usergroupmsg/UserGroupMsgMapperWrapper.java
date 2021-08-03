package com.lhever.simpleim.router.dao.mapper.usergroupmsg;

import com.lhever.simpleim.common.pojo.UserGroupMsg;
import com.lhever.simpleim.common.pojo.UserGroupMsgExample;

import java.util.Date;
import java.util.List;

public class UserGroupMsgMapperWrapper {

    private UserGroupMsgMapper userGroupMsgMapper;

    public UserGroupMsgMapperWrapper(UserGroupMsgMapper userGroupMsgMapper) {
        this.userGroupMsgMapper = userGroupMsgMapper;
    }


    public long countByExample(UserGroupMsgExample example) {
        return userGroupMsgMapper.countByExample(example);
    }


    public int deleteByExample(UserGroupMsgExample example) {
        return userGroupMsgMapper.deleteByExample(example);
    }


    public int deleteByPrimaryKey(String id) {
        return userGroupMsgMapper.deleteByPrimaryKey(id);
    }


    public int insert(UserGroupMsg record) {
        return userGroupMsgMapper.insert(record);
    }


    public int insertSelective(UserGroupMsg record) {
        return userGroupMsgMapper.insertSelective(record);
    }


    public List<UserGroupMsg> selectByExample(UserGroupMsgExample example) {
        return userGroupMsgMapper.selectByExample(example);
    }


    public UserGroupMsg selectByPrimaryKey(String id) {
        return userGroupMsgMapper.selectByPrimaryKey(id);
    }


    public int updateByExampleSelective(UserGroupMsg record, UserGroupMsgExample example) {
        return userGroupMsgMapper.updateByExampleSelective(record, example);
    }


    public int updateByExample(UserGroupMsg record, UserGroupMsgExample example) {
        return userGroupMsgMapper.updateByExample(record, example);
    }


    public int updateByPrimaryKeySelective(UserGroupMsg record) {
        return userGroupMsgMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(UserGroupMsg record) {
        return userGroupMsgMapper.updateByPrimaryKey(record);
    }


    public int updateStatusById(String id, Integer status) {
        UserGroupMsg update = new UserGroupMsg();
        update.setId(id);
        update.setStatus(status);
        update.setUpdateTime(new Date());
        return userGroupMsgMapper.updateByPrimaryKeySelective(update);
    }
}