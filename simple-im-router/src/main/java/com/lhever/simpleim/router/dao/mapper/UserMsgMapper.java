package com.lhever.simpleim.router.dao.mapper;

import com.lhever.simpleim.router.pojo.UserMsg;
import com.lhever.simpleim.router.pojo.UserMsgExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface UserMsgMapper {
    @SelectProvider(type=UserMsgSqlProvider.class, method="countByExample")
    long countByExample(UserMsgExample example);

    @DeleteProvider(type=UserMsgSqlProvider.class, method="deleteByExample")
    int deleteByExample(UserMsgExample example);

    @Delete({
        "delete from im_user_msg",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String id);

    @Insert({
        "insert into im_user_msg (id, create_id, ",
        "receive_id, type, ",
        "content, send_status, ",
        "read_status, create_time, ",
        "update_time)",
        "values (#{id,jdbcType=VARCHAR}, #{createId,jdbcType=VARCHAR}, ",
        "#{receiveId,jdbcType=VARCHAR}, #{type,jdbcType=INTEGER}, ",
        "#{content,jdbcType=VARCHAR}, #{sendStatus,jdbcType=INTEGER}, ",
        "#{readStatus,jdbcType=INTEGER}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(UserMsg record);

    @InsertProvider(type=UserMsgSqlProvider.class, method="insertSelective")
    int insertSelective(UserMsg record);

    @SelectProvider(type=UserMsgSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="create_id", property="createId", jdbcType=JdbcType.VARCHAR),
        @Result(column="receive_id", property="receiveId", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.INTEGER),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="send_status", property="sendStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="read_status", property="readStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<UserMsg> selectByExample(UserMsgExample example);

    @Select({
        "select",
        "id, create_id, receive_id, type, content, send_status, read_status, create_time, ",
        "update_time",
        "from im_user_msg",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="create_id", property="createId", jdbcType=JdbcType.VARCHAR),
        @Result(column="receive_id", property="receiveId", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.INTEGER),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="send_status", property="sendStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="read_status", property="readStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    UserMsg selectByPrimaryKey(String id);

    @UpdateProvider(type=UserMsgSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") UserMsg record, @Param("example") UserMsgExample example);

    @UpdateProvider(type=UserMsgSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") UserMsg record, @Param("example") UserMsgExample example);

    @UpdateProvider(type=UserMsgSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UserMsg record);

    @Update({
        "update im_user_msg",
        "set create_id = #{createId,jdbcType=VARCHAR},",
          "receive_id = #{receiveId,jdbcType=VARCHAR},",
          "type = #{type,jdbcType=INTEGER},",
          "content = #{content,jdbcType=VARCHAR},",
          "send_status = #{sendStatus,jdbcType=INTEGER},",
          "read_status = #{readStatus,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(UserMsg record);
}