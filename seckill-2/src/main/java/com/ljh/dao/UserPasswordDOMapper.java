package com.ljh.dao;

import com.ljh.entity.UserPasswordDO;
import com.ljh.entity.UserPasswordDOExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserPasswordDOMapper {
    long countByExample(UserPasswordDOExample example);

    int deleteByExample(UserPasswordDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserPasswordDO row);

    int insertSelective(UserPasswordDO row);

    List<UserPasswordDO> selectByExample(UserPasswordDOExample example);

    UserPasswordDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") UserPasswordDO row, @Param("example") UserPasswordDOExample example);

    int updateByExample(@Param("row") UserPasswordDO row, @Param("example") UserPasswordDOExample example);

    int updateByPrimaryKeySelective(UserPasswordDO row);

    int updateByPrimaryKey(UserPasswordDO row);
}