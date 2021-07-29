package com.lhever.simpleim.router.dao.mapper.group;

import com.lhever.simpleim.common.pojo.Group;
import com.lhever.simpleim.common.pojo.GroupExample;

import java.util.List;

public class GroupMapperWrapper {

    private GroupMapper groupMapper;

    public GroupMapperWrapper(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }


    public long countByExample(GroupExample example) {
        return groupMapper.countByExample(example);
    }


    public int deleteByExample(GroupExample example) {
        return groupMapper.deleteByExample(example);
    }


    public int deleteByPrimaryKey(String id) {
        return groupMapper.deleteByPrimaryKey(id);
    }


    public int insert(Group record) {
        return groupMapper.insert(record);
    }


    public int insertSelective(Group record) {
        return groupMapper.insertSelective(record);
    }


    public List<Group> selectByExample(GroupExample example) {
        return groupMapper.selectByExample(example);
    }


    public Group selectByPrimaryKey(String id) {
        return groupMapper.selectByPrimaryKey(id);
    }


    public int updateByExampleSelective(Group record, GroupExample example) {
        return groupMapper.updateByExampleSelective(record, example);
    }


    public int updateByExample(Group record, GroupExample example) {
        return groupMapper.updateByExample(record, example);
    }


    public int updateByPrimaryKeySelective(Group record) {
        return groupMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(Group record) {
        return groupMapper.updateByPrimaryKey(record);
    }
}