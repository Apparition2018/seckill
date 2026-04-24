package com.ljh.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class ItemDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Setter
    private Integer id;
    private String title;
    @Setter
    private BigDecimal price;
    private String description;
    @Setter
    private Integer sales;
    private String imgUrl;

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }
}
