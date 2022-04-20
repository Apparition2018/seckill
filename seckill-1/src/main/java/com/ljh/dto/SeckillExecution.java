package com.ljh.dto;

import com.ljh.entity.SuccessKilled;
import com.ljh.enums.SeckillStatEnum;
import lombok.Data;

/**
 * 封装秒杀执行后结果
 */
@Data
public class SeckillExecution {

    // 秒杀 ID
    private long seckillId;
    // 秒杀执行结果状态
    private int state;
    // 状态表示
    private String stateInfo;
    // 秒杀成功对象
    private SuccessKilled successKilled;

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }
}
