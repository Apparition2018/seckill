package com.ljh.dto;

import lombok.Data;

/**
 * 暴露秒杀地址 DTO
 */
@Data
public class Exposer {

    // 是否开启秒杀
    private boolean exposed;
    // 秒杀 ID 加盐 MD5 加密，用于验权
    private String md5;
    // 秒杀 ID
    private long seckillId;
    // 系统当前时间
    private long now;
    // 开始时间
    private long start;
    // 结束时间
    private long end;

    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, long seckillId, long now, long start, long end) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }
}
