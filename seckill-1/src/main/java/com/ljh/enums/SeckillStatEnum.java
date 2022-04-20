package com.ljh.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 使用枚举表述常量数据字段
 */
@Getter
@AllArgsConstructor
public enum SeckillStatEnum {

    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATA_REWRITE(-3, "数据篡改");

    private final int state;
    private final String stateInfo;

    public static SeckillStatEnum stateOf(int index) {
        for (SeckillStatEnum seckillStatEnum : values()) {
            if (seckillStatEnum.getState() == index) return seckillStatEnum;
        }
        return null;
    }
}
