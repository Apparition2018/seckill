package com.ljh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Seckill implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long seckillId;
    private String name;
    private Integer number;
    private Date startTime;
    private Date endTime;
    private Date createTime;
}