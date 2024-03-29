package com.ljh.service;

import com.ljh.error.BusinessException;
import com.ljh.service.model.OrderModel;

public interface OrderService {

    /**
     * 1.通过前端 URL 上传过来秒杀活动 ID，然后下单接口内校验对应 ID 是否属于对应商品且活动已开始 (推荐使用，因为一个商品可能存在多个秒杀活动)
     * 2.直接在下单接口内判断对应的商品是否存在秒杀活动，若存在进行中的则以活动价格下单
     */
    OrderModel createOrder(Integer userId, Integer itemId, Integer PromoId, Integer amount) throws BusinessException;
}
