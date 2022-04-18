package com.ljh.dao;

import com.ljh.entity.Seckill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface SeckillDao {

    /**
     * 减库存
     *
     * @param seckillId 秒杀ID
     * @param killTime  秒杀时间
     * @return 表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") Long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据 ID 查询秒杀对象
     *
     * @param seckillId 秒杀ID
     */
    Seckill queryById(Long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 使用存储过程执行秒杀
     */
    void killByProcedure(Map<String, Object> paramMap);


    int deleteByPrimaryKey(Long seckillId);

    int insert(Seckill record);

    int insertSelective(Seckill record);

    Seckill selectByPrimaryKey(Long seckillId);

    int updateByPrimaryKeySelective(Seckill record);

    int updateByPrimaryKey(Seckill record);
}