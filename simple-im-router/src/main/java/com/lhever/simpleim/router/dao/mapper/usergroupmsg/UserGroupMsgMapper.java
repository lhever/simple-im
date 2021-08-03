package com.lhever.simpleim.router.dao.mapper.usergroupmsg;

import com.lhever.simpleim.common.pojo.UserGroupMsg;
import com.lhever.simpleim.common.pojo.UserGroupMsgExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface UserGroupMsgMapper {
    @SelectProvider(type= UserGroupMsgSqlProvider.class, method="countByExample")
    long countByExample(UserGroupMsgExample example);

    @DeleteProvider(type= UserGroupMsgSqlProvider.class, method="deleteByExample")
    int deleteByExample(UserGroupMsgExample example);

    @Delete({
        "delete from im_user_group_msg",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String id);

    @Insert({
        "insert into im_user_group_msg (id, create_id, ",
        "group_id, receive_id, ",
        "type, content, status, ",
        "create_time, update_time)",
        "values (#{id,jdbcType=VARCHAR}, #{createId,jdbcType=VARCHAR}, ",
        "#{groupId,jdbcType=VARCHAR}, #{receiveId,jdbcType=VARCHAR}, ",
        "#{type,jdbcType=INTEGER}, #{content,jdbcType=VARCHAR}, #{status,jdbcType=INTEGER}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(UserGroupMsg record);

    @InsertProvider(type= UserGroupMsgSqlProvider.class, method="insertSelective")
    int insertSelective(UserGroupMsg record);

    @SelectProvider(type= UserGroupMsgSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="create_id", property="createId", jdbcType=JdbcType.VARCHAR),
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.VARCHAR),
        @Result(column="receive_id", property="receiveId", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.INTEGER),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<UserGroupMsg> selectByExample(UserGroupMsgExample example);

    @Select({
        "select",
        "id, create_id, group_id, receive_id, type, content, status, create_time, update_time",
        "from im_user_group_msg",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="create_id", property="createId", jdbcType=JdbcType.VARCHAR),
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.VARCHAR),
        @Result(column="receive_id", property="receiveId", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.INTEGER),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    UserGroupMsg selectByPrimaryKey(String id);

    @UpdateProvider(type= UserGroupMsgSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") UserGroupMsg record, @Param("example") UserGroupMsgExample example);

    @UpdateProvider(type= UserGroupMsgSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") UserGroupMsg record, @Param("example") UserGroupMsgExample example);

    @UpdateProvider(type= UserGroupMsgSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UserGroupMsg record);

    @Update({
        "update im_user_group_msg",
        "set create_id = #{createId,jdbcType=VARCHAR},",
          "group_id = #{groupId,jdbcType=VARCHAR},",
          "receive_id = #{receiveId,jdbcType=VARCHAR},",
          "type = #{type,jdbcType=INTEGER},",
          "content = #{content,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(UserGroupMsg record);




}