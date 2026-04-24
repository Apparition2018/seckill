package com.ljh.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class OrderDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String id;
    @Setter
    private Integer userId;
    @Setter
    private Integer itemId;
    @Setter
    private Integer promoId;
    @Setter
    private BigDecimal itemPrice;
    @Setter
    private Integer amount;
    @Setter
    private BigDecimal orderPrice;

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }
}
