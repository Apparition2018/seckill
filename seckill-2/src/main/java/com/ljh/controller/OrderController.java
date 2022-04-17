package com.ljh.controller;

import com.ljh.error.BusinessException;
import com.ljh.error.BusinessErrorEnum;
import com.ljh.response.CommonReturnType;
import com.ljh.service.OrderService;
import com.ljh.service.model.OrderModel;
import com.ljh.service.model.UserModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController("/order")
@RequestMapping("/order")
public class OrderController extends BaseController {

    private final OrderService orderService;
    private final HttpServletRequest httpServletRequest;

    public OrderController(OrderService orderService, HttpServletRequest httpServletRequest) {
        this.orderService = orderService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * 下单
     */
    @RequestMapping(value = "/createOrder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
                                        @RequestParam(name = "amount") Integer amount,
                                        @RequestParam(name = "promoId", required = false) Integer promoId) throws BusinessException {
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (isLogin == null || !isLogin)
            throw new BusinessException(BusinessErrorEnum.USER_NOT_LOGIN, "用户还未登录，不能下单");
        // 获取用户的登录信息
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, promoId, amount);
        return CommonReturnType.create(null);
    }
}
