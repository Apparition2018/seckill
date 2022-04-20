package com.ljh.service;

import com.ljh.dto.Exposer;
import com.ljh.dto.SeckillExecution;
import com.ljh.entity.Seckill;
import com.ljh.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在"使用者"角度设计接口
 * 三个方面：1.方法定义粒度；2.参数；3.返回类型（return 类型/异常）
 */
public interface SeckillService {

    /**
     * 查询所有秒杀记录
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开始时，输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException;

    /**
     * 执行秒杀操作 by 存储过程
     */
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) throws SeckillException;
}
