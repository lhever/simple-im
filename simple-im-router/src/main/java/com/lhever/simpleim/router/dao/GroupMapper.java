package com.lhever.simpleim.router.dao;

import com.lhever.simpleim.router.pojo.Group;
import com.lhever.simpleim.router.pojo.GroupExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface GroupMapper {
    @SelectProvider(type= GroupSqlProvider.class, method="countByExample")
    long countByExample(GroupExample example);

    @DeleteProvider(type= GroupSqlProvider.class, method="deleteByExample")
    int deleteByExample(GroupExample example);

    @Delete({
        "delete from im_group",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String id);

    @Insert({
        "insert into im_group (id, group_name, ",
        "create_id, status, ",
        "create_time, update_time)",
        "values (#{id,jdbcType=VARCHAR}, #{groupName,jdbcType=VARCHAR}, ",
        "#{createId,jdbcType=VARCHAR}, #{status,jdbcType=INTEGER}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(Group record);

    @InsertProvider(type= GroupSqlProvider.class, method="insertSelective")
    int insertSelective(Group record);

    @SelectProvider(type= GroupSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="group_name", property="groupName", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_id", property="createId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<Group> selectByExample(GroupExample example);

    @Select({
        "select",
        "id, group_name, create_id, status, create_time, update_time",
        "from im_group",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="group_name", property="groupName", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_id", property="createId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    Group selectByPrimaryKey(String id);

    @UpdateProvider(type= GroupSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Group record, @Param("example") GroupExample example);

    @UpdateProvider(type= GroupSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Group record, @Param("example") GroupExample example);

    @UpdateProvider(type= GroupSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Group record);

    @Update({
        "update im_group",
        "set group_name = #{groupName,jdbcType=VARCHAR},",
          "create_id = #{createId,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(Group record);
}