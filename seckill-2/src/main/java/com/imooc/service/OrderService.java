package com.imooc.service;

import com.imooc.error.BusinessException;
import com.imooc.service.model.OrderModel;

public interface OrderService {

    // 1.通过前端url上传过来秒杀活动id，然后下单接口内校验对应id是否属于对应商品且活动已开始 (使用)
    // 2.直接在下单接口内判断对应的商品是否存在秒杀活动，若存在进行中的则以活动价格下单
    OrderModel createOrder(Integer userId, Integer itemId, Integer PromoId, Integer amount) throws BusinessException;



}
