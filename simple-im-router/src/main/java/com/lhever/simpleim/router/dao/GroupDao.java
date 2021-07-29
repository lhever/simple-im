package com.lhever.simpleim.router.dao;


import com.lhever.common.core.utils.CollectionUtils;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.simpleim.common.msg.CreateGroupReq;
import com.lhever.simpleim.common.pojo.Group;
import com.lhever.simpleim.common.pojo.UserGroup;
import com.lhever.simpleim.router.basic.cfg.SessionFactoryHolder;
import com.lhever.simpleim.router.dao.mapper.group.GroupMapper;
import com.lhever.simpleim.router.dao.mapper.group.GroupMapperWrapper;
import com.lhever.simpleim.router.dao.mapper.usergroup.UserGroupMapper;
import com.lhever.simpleim.router.dao.mapper.usergroup.UserGroupMapperWrapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GroupDao {

    @Autowired
    private SessionFactoryHolder sessionFactoryHolder;


    public void createGroup(CreateGroupReq req) {
        Group group = createGroup(req.getGroupName(), req.getCreateId());
        List<UserGroup> userGroups = createUserGroup(group, req.getUserIds());

        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sessionFactory = sessionFactoryHolder.getSessionFactory(group.getId());
            sqlSession = sessionFactory.openSession();
            GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
            GroupMapperWrapper wrapper = new GroupMapperWrapper(groupMapper);
            wrapper.insert(group);

            UserGroupMapper userGroupMapper = sqlSession.getMapper(UserGroupMapper.class);
            UserGroupMapperWrapper userGroupMapperWrapper = new UserGroupMapperWrapper(userGroupMapper);
            for (UserGroup userGroup : userGroups) {
                userGroupMapperWrapper.insert(userGroup);
            }
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }

    public Group createGroup(String groupName, String createId) {
        Group group = new Group();
        group.setId(StringUtils.getUuid());
        group.setGroupName(groupName);
        group.setCreateId(createId);
        group.setStatus(0);
        Date now = new Date();
        group.setCreateTime(now);
        group.setUpdateTime(now);
        return group;
    }

    public List<UserGroup> createUserGroup(Group group, List<String> userIds) {
        userIds.add(group.getCreateId());
        userIds = CollectionUtils.removeRepeat(userIds);

        Date now = new Date();
        List<UserGroup> userGroups = new ArrayList<>();
        for (String userId : userIds) {
            UserGroup userGroup = new UserGroup();
            userGroup.setId(StringUtils.getUuid());
            userGroup.setGroupId(group.getId());
            userGroup.setUserId(userId);
            userGroup.setCreateTime(now);
            userGroup.setUpdateTime(now);
            userGroups.add(userGroup);
        }
        return userGroups;
    }















}
