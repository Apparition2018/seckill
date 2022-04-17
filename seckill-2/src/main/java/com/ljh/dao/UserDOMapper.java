package com.ljh.dao;

import com.ljh.entity.UserDO;
import com.ljh.entity.UserDOExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDOMapper {
    long countByExample(UserDOExample example);

    int deleteByExample(UserDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserDO row);

    int insertSelective(UserDO row);

    List<UserDO> selectByExample(UserDOExample example);

    UserDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") UserDO row, @Param("example") UserDOExample example);

    int updateByExample(@Param("row") UserDO row, @Param("example") UserDOExample example);

    int updateByPrimaryKeySelective(UserDO row);

    int updateByPrimaryKey(UserDO row);
}