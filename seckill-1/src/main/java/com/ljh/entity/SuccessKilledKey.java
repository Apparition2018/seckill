package com.ljh.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SuccessKilledKey implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long seckillId;
    private Long userPhone;
}