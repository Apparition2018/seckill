package com.ljh.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
public class PromoDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Setter
    private Integer id;
    private String promoName;
    @Setter
    private Date startDate;
    @Setter
    private Date endDate;
    @Setter
    private Integer itemId;
    @Setter
    private BigDecimal promoItemPrice;

    public void setPromoName(String promoName) {
        this.promoName = promoName == null ? null : promoName.trim();
    }
}
