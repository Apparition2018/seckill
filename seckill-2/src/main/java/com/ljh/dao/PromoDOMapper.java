package com.ljh.dao;

import com.ljh.entity.PromoDO;
import com.ljh.entity.PromoDOExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromoDOMapper {
    long countByExample(PromoDOExample example);

    int deleteByExample(PromoDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PromoDO row);

    int insertSelective(PromoDO row);

    List<PromoDO> selectByExample(PromoDOExample example);

    PromoDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") PromoDO row, @Param("example") PromoDOExample example);

    int updateByExample(@Param("row") PromoDO row, @Param("example") PromoDOExample example);

    int updateByPrimaryKeySelective(PromoDO row);

    int updateByPrimaryKey(PromoDO row);
}