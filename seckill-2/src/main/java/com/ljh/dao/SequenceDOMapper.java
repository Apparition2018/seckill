package com.ljh.dao;

import com.ljh.entity.SequenceDO;
import com.ljh.entity.SequenceDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SequenceDOMapper {
    long countByExample(SequenceDOExample example);

    int deleteByExample(SequenceDOExample example);

    int deleteByPrimaryKey(String name);

    int insert(SequenceDO row);

    int insertSelective(SequenceDO row);

    List<SequenceDO> selectByExample(SequenceDOExample example);

    SequenceDO selectByPrimaryKey(String name);

    int updateByExampleSelective(@Param("row") SequenceDO row, @Param("example") SequenceDOExample example);

    int updateByExample(@Param("row") SequenceDO row, @Param("example") SequenceDOExample example);

    int updateByPrimaryKeySelective(SequenceDO row);

    int updateByPrimaryKey(SequenceDO row);
}