package com.lhever.simpleim.router.dao;


import com.lhever.common.core.utils.CollectionUtils;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.simpleim.common.dto.router.RouterCreateGroupReq;
import com.lhever.simpleim.common.dto.router.RouterCreateGroupResp;
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


    public RouterCreateGroupResp createGroup(RouterCreateGroupReq req) {
        List<String> userIds = req.getUserIds();
        if (userIds == null) {
            userIds = new ArrayList<>(1);
        }
        userIds.add(req.getCreateId());
        userIds = CollectionUtils.removeRepeat(userIds);

        Group group = createGroup(req.getGroupName(), req.getCreateId(), userIds);
        List<UserGroup> userGroups = createUserGroup(group, userIds);
       saveGroup(group);
       saveUserGroup(userGroups);

        RouterCreateGroupResp result = new RouterCreateGroupResp();
        result.setGroupId(group.getId());
        result.setCreateId(group.getCreateId());
        result.setUserIds(userIds);
        return result;
    }


    public void saveGroup(Group group) {
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sessionFactory = sessionFactoryHolder.getSessionFactory(group.getId());
            sqlSession = sessionFactory.openSession();
            GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
            GroupMapperWrapper wrapper = new GroupMapperWrapper(groupMapper);
            wrapper.insert(group);
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }


    public void saveUserGroup(List<UserGroup> userGroups) {
        List<SqlSession> sqlSessions = new ArrayList<>();
        try {
            for (UserGroup userGroup : userGroups) {
                SqlSessionFactory sessionFactory = sessionFactoryHolder.getSessionFactory(userGroup.getUserId());
                SqlSession sqlSession = sessionFactory.openSession();
                sqlSessions.add(sqlSession);

                UserGroupMapper userGroupMapper = sqlSession.getMapper(UserGroupMapper.class);
                UserGroupMapperWrapper userGroupMapperWrapper = new UserGroupMapperWrapper(userGroupMapper);
                userGroupMapperWrapper.insert(userGroup);
            }
        } finally {
            for (SqlSession sqlSession : sqlSessions) {
                sqlSession.commit();
                sqlSession.close();
            }
        }
    }


    public Group createGroup(String groupName, String createId, List<String> memberIds) {
        Group group = new Group();
        group.setId(StringUtils.getUuid());
        group.setGroupName(groupName);
        group.setCreateId(createId);
        group.setMemberIds(StringUtils.join(memberIds, ","));
        group.setStatus(0);
        Date now = new Date();
        group.setCreateTime(now);
        group.setUpdateTime(now);
        return group;
    }

    public List<UserGroup> createUserGroup(Group group, List<String> userIds) {
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



    public Group findGroup(String groupId) {
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory aDefault = sessionFactoryHolder.getSessionFactory(groupId);
            sqlSession = aDefault.openSession();
            GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
            GroupMapperWrapper wrapper = new GroupMapperWrapper(groupMapper);
            return  wrapper.selectByPrimaryKey(groupId);
        } finally {
            sqlSession.commit();
            sqlSession.close();

        }
    }


}
