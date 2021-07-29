package com.lhever.simpleim.router.dao.mapper.groupmsg;

import com.lhever.simpleim.common.pojo.GroupMsg;
import com.lhever.simpleim.common.pojo.GroupMsgExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface GroupMsgMapper {
    @SelectProvider(type=GroupMsgSqlProvider.class, method="countByExample")
    long countByExample(GroupMsgExample example);

    @DeleteProvider(type=GroupMsgSqlProvider.class, method="deleteByExample")
    int deleteByExample(GroupMsgExample example);

    @Delete({
        "delete from im_group_msg",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String id);

    @Insert({
        "insert into im_group_msg (id, create_id, ",
        "group_id, receive_ids, ",
        "type, content, send_status, ",
        "read_status, create_time, ",
        "update_time)",
        "values (#{id,jdbcType=VARCHAR}, #{createId,jdbcType=VARCHAR}, ",
        "#{groupId,jdbcType=VARCHAR}, #{receiveIds,jdbcType=VARCHAR}, ",
        "#{type,jdbcType=INTEGER}, #{content,jdbcType=VARCHAR}, #{sendStatus,jdbcType=INTEGER}, ",
        "#{readStatus,jdbcType=INTEGER}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(GroupMsg record);

    @InsertProvider(type=GroupMsgSqlProvider.class, method="insertSelective")
    int insertSelective(GroupMsg record);

    @SelectProvider(type=GroupMsgSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="create_id", property="createId", jdbcType=JdbcType.VARCHAR),
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.VARCHAR),
        @Result(column="receive_ids", property="receiveIds", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.INTEGER),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="send_status", property="sendStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="read_status", property="readStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<GroupMsg> selectByExample(GroupMsgExample example);

    @Select({
        "select",
        "id, create_id, group_id, receive_ids, type, content, send_status, read_status, ",
        "create_time, update_time",
        "from im_group_msg",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="create_id", property="createId", jdbcType=JdbcType.VARCHAR),
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.VARCHAR),
        @Result(column="receive_ids", property="receiveIds", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.INTEGER),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="send_status", property="sendStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="read_status", property="readStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    GroupMsg selectByPrimaryKey(String id);

    @UpdateProvider(type=GroupMsgSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") GroupMsg record, @Param("example") GroupMsgExample example);

    @UpdateProvider(type=GroupMsgSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") GroupMsg record, @Param("example") GroupMsgExample example);

    @UpdateProvider(type=GroupMsgSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(GroupMsg record);

    @Update({
        "update im_group_msg",
        "set create_id = #{createId,jdbcType=VARCHAR},",
          "group_id = #{groupId,jdbcType=VARCHAR},",
          "receive_ids = #{receiveIds,jdbcType=VARCHAR},",
          "type = #{type,jdbcType=INTEGER},",
          "content = #{content,jdbcType=VARCHAR},",
          "send_status = #{sendStatus,jdbcType=INTEGER},",
          "read_status = #{readStatus,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(GroupMsg record);
}