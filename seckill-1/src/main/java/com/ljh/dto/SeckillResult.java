package com.ljh.dto;

import lombok.Data;

/**
 * 所有 Ajax 请求返回类型，封装 Json 结果
 */
@Data
public class SeckillResult<T> {

    private boolean success;
    private T data;
    private String error;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }
}
