package com.lhever.simpleim.router.dao.mapper.usergroup;

import com.lhever.simpleim.common.pojo.UserGroup;
import com.lhever.simpleim.common.pojo.UserGroupExample;

import java.util.List;

public class UserGroupMapperWrapper {

    private UserGroupMapper userGroupMapper;

    public UserGroupMapperWrapper(UserGroupMapper userGroupMapper) {
        this.userGroupMapper = userGroupMapper;
    }


    public long countByExample(UserGroupExample example) {
        return userGroupMapper.countByExample(example);
    }


    public int deleteByExample(UserGroupExample example) {
        return userGroupMapper.deleteByExample(example);
    }


    public int deleteByPrimaryKey(String id) {
        return userGroupMapper.deleteByPrimaryKey(id);
    }


    public int insert(UserGroup record) {
        return userGroupMapper.insert(record);
    }


    public int insertSelective(UserGroup record) {
        return userGroupMapper.insertSelective(record);
    }


    public List<UserGroup> selectByExample(UserGroupExample example) {
        return userGroupMapper.selectByExample(example);
    }


    public UserGroup selectByPrimaryKey(String id) {
        return userGroupMapper.selectByPrimaryKey(id);
    }


    public int updateByExampleSelective(UserGroup record, UserGroupExample example) {
        return userGroupMapper.updateByExampleSelective(record, example);
    }


    public int updateByExample(UserGroup record, UserGroupExample example) {
        return userGroupMapper.updateByExample(record, example);
    }


    public int updateByPrimaryKeySelective(UserGroup record) {
        return userGroupMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(UserGroup record) {
        return userGroupMapper.updateByPrimaryKey(record);
    }
}