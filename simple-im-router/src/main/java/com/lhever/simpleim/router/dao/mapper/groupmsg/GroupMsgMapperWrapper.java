package com.lhever.simpleim.router.dao.mapper.groupmsg;

import com.lhever.simpleim.common.pojo.GroupMsg;
import com.lhever.simpleim.common.pojo.GroupMsgExample;

import java.util.List;

public class GroupMsgMapperWrapper {

    private GroupMsgMapper groupMsgMapper;

    public GroupMsgMapperWrapper(GroupMsgMapper groupMsgMapper) {
        this.groupMsgMapper = groupMsgMapper;
    }


    public long countByExample(GroupMsgExample example) {
        return groupMsgMapper.countByExample(example);
    }


    public int deleteByExample(GroupMsgExample example) {
        return groupMsgMapper.deleteByExample(example);
    }


    public int deleteByPrimaryKey(String id) {
        return groupMsgMapper.deleteByPrimaryKey(id);
    }


    public int insert(GroupMsg record) {
        return groupMsgMapper.insert(record);
    }


    public int insertSelective(GroupMsg record) {
        return groupMsgMapper.insertSelective(record);
    }


    public List<GroupMsg> selectByExample(GroupMsgExample example) {
        return groupMsgMapper.selectByExample(example);
    }


    public GroupMsg selectByPrimaryKey(String id) {
        return groupMsgMapper.selectByPrimaryKey(id);
    }


    public int updateByExampleSelective(GroupMsg record, GroupMsgExample example) {
        return groupMsgMapper.updateByExampleSelective(record, example);
    }


    public int updateByExample(GroupMsg record, GroupMsgExample example) {
        return groupMsgMapper.updateByExample(record, example);
    }


    public int updateByPrimaryKeySelective(GroupMsg record) {
        return groupMsgMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(GroupMsg record) {
        return groupMsgMapper.updateByPrimaryKey(record);
    }


    public int incrReadCount(String id) {

        return groupMsgMapper.incrReadCount(id);
    }
}