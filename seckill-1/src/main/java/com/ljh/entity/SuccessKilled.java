package com.ljh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SuccessKilled extends SuccessKilledKey implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer state;
    private Date createTime;
    private Seckill seckill;
}