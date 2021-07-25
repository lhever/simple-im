package com.lhever.simpleim.router.dao.mapper;

import com.lhever.simpleim.common.pojo.UserGroup;
import com.lhever.simpleim.common.pojo.UserGroupExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface UserGroupMapper {
    @SelectProvider(type=UserGroupSqlProvider.class, method="countByExample")
    long countByExample(UserGroupExample example);

    @DeleteProvider(type=UserGroupSqlProvider.class, method="deleteByExample")
    int deleteByExample(UserGroupExample example);

    @Delete({
        "delete from im_user_group",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String id);

    @Insert({
        "insert into im_user_group (id, group_id, ",
        "user_id, create_time, ",
        "update_time)",
        "values (#{id,jdbcType=VARCHAR}, #{groupId,jdbcType=VARCHAR}, ",
        "#{userId,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(UserGroup record);

    @InsertProvider(type=UserGroupSqlProvider.class, method="insertSelective")
    int insertSelective(UserGroup record);

    @SelectProvider(type=UserGroupSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<UserGroup> selectByExample(UserGroupExample example);

    @Select({
        "select",
        "id, group_id, user_id, create_time, update_time",
        "from im_user_group",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    UserGroup selectByPrimaryKey(String id);

    @UpdateProvider(type=UserGroupSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") UserGroup record, @Param("example") UserGroupExample example);

    @UpdateProvider(type=UserGroupSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") UserGroup record, @Param("example") UserGroupExample example);

    @UpdateProvider(type=UserGroupSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UserGroup record);

    @Update({
        "update im_user_group",
        "set group_id = #{groupId,jdbcType=VARCHAR},",
          "user_id = #{userId,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(UserGroup record);
}