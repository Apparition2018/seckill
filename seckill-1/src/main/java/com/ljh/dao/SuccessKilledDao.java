package com.ljh.dao;

import com.ljh.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

public interface SuccessKilledDao {

    /**
     * 插入购买明细，可过滤重复(联合主键)
     *
     * @param seckillId 秒杀ID
     * @param userPhone 用户手机号
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") Long seckillId, @Param("userPhone") Long userPhone);

    /**
     * 根据 ID 查询 SuccessKilled 并携带秒杀产品对象实体 Seckill
     *
     * @param seckillId 秒杀ID
     * @param userPhone 用户手机号
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") Long seckillId, @Param("userPhone") Long userPhone);
}