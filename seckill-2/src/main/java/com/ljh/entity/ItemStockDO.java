package com.ljh.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class ItemStockDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer stock;
    private Integer itemId;
}
