package com.ljh.dao;

import com.ljh.entity.ItemStockDO;
import com.ljh.entity.ItemStockDOExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ItemStockDOMapper {
    long countByExample(ItemStockDOExample example);

    int deleteByExample(ItemStockDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ItemStockDO row);

    int insertSelective(ItemStockDO row);

    List<ItemStockDO> selectByExample(ItemStockDOExample example);

    ItemStockDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") ItemStockDO row, @Param("example") ItemStockDOExample example);

    int updateByExample(@Param("row") ItemStockDO row, @Param("example") ItemStockDOExample example);

    int updateByPrimaryKeySelective(ItemStockDO row);

    int updateByPrimaryKey(ItemStockDO row);

    @Update({
            "UPDATE item_stock SET stock = stock - #{amount}",
            "WHERE item_id = #{itemId} AND stock >= #{amount}"
    })
    int decreaseStock(@Param("itemId") Integer itemId, @Param("amount") Integer amount);
}