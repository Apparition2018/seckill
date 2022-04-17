package com.ljh.dao;

import com.ljh.entity.OrderDO;
import com.ljh.entity.OrderDOExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDOMapper {
    long countByExample(OrderDOExample example);

    int deleteByExample(OrderDOExample example);

    int deleteByPrimaryKey(String id);

    int insert(OrderDO row);

    int insertSelective(OrderDO row);

    List<OrderDO> selectByExample(OrderDOExample example);

    OrderDO selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") OrderDO row, @Param("example") OrderDOExample example);

    int updateByExample(@Param("row") OrderDO row, @Param("example") OrderDOExample example);

    int updateByPrimaryKeySelective(OrderDO row);

    int updateByPrimaryKey(OrderDO row);
}