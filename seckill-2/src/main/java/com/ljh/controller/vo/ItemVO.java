package com.ljh.controller.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemVO {

    private Integer id;
    // 商品名称
    private String title;
    // 商品价格
    private BigDecimal price;
    // 商品库存
    private Integer stock;
    // 商品描述
    private String description;
    // 商品销量
    private Integer sales;
    // 商品描述图片的url
    private String imgUrl;
    // 商品是否在秒杀活动中，以及对应的状态（1：没有秒杀活动 2：秒杀活动待开始 3：秒杀活动进行中）
    private Integer promoStatus;
    // 秒杀活动价格
    private BigDecimal promoPrice;
    // 秒杀活动ID
    private Integer promoId;
    // 秒杀活动开始时间
    private String startDate;
}
