package com.ljh.dao;

import com.ljh.entity.ItemDO;
import com.ljh.entity.ItemDOExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ItemDOMapper {
    long countByExample(ItemDOExample example);

    int deleteByExample(ItemDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO row);

    int insertSelective(ItemDO row);

    List<ItemDO> selectByExample(ItemDOExample example);

    ItemDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") ItemDO row, @Param("example") ItemDOExample example);

    int updateByExample(@Param("row") ItemDO row, @Param("example") ItemDOExample example);

    int updateByPrimaryKeySelective(ItemDO row);

    int updateByPrimaryKey(ItemDO row);

    @Update("UPDATE item SET sales = sales +#{amount} WHERE id = #{id}")
    int increaseSales(Integer itemId, Integer amount);
}