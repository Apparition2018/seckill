package com.ljh.service.impl;

import com.ljh.dao.OrderDOMapper;
import com.ljh.dao.SequenceDOMapper;
import com.ljh.entity.OrderDO;
import com.ljh.entity.SequenceDO;
import com.ljh.entity.SequenceDOExample;
import com.ljh.error.BusinessException;
import com.ljh.error.BusinessErrorEnum;
import com.ljh.service.ItemService;
import com.ljh.service.OrderService;
import com.ljh.service.UserService;
import com.ljh.service.model.ItemModel;
import com.ljh.service.model.OrderModel;
import com.ljh.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {

    private final ItemService itemService;
    private final UserService userService;
    private final OrderDOMapper orderDOMapper;
    private final SequenceDOMapper sequenceDOMapper;

    public OrderServiceImpl(ItemService itemService, UserService userService, OrderDOMapper orderDOMapper, SequenceDOMapper sequenceDOMapper) {
        this.itemService = itemService;
        this.userService = userService;
        this.orderDOMapper = orderDOMapper;
        this.sequenceDOMapper = sequenceDOMapper;
    }

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
        // 1.检验
        // 1.1 下单的商品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null)
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        UserModel userModel = userService.getUserById(userId);
        if (userModel == null)
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        if (amount <= 0 || amount > 99)
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "数量信息不正确");

        // 1.2 校验活动信息
        if (promoId != null) {
            // 1.2.1 校验对应活动是否存在这个适用商品
            if (promoId.intValue() != itemModel.getPromoModel().getId()) {
                throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
                // 1.2.2 校验活动是否正在进行中
            } else if (itemModel.getPromoModel().getStatus() != 2) {
                throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "活动还未开始");
            }
        }

        // 2.减库存方式：①下单减库存；②付款减库存
        // 这里使用下单减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) throw new BusinessException(BusinessErrorEnum.STOCK_NOT_ENOUGH);

        // 3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if (promoId != null) {
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setPromoId(promoId);
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));

        // 生成交易流水号，订单号
        orderModel.setId(this.generateOrderNo());
        OrderDO orderDO = this.convertEntityFromModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        // 商品的销量增加
        itemService.increaseSales(itemId, amount);

        // 4.返回前端
        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String generateOrderNo() {
        // 订单号有16位
        StringBuilder stringBuilder = new StringBuilder();
        // 1.前8位为年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);

        // 2.中间6位为自增序列
        // 从序列表 sequence_info 获取
        int sequence;
        SequenceDO sequenceDO = this.selectSequenceDOByName();
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKey(sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        // 3.最后2位为分库分表位
        // 比如 userId % 100，这里暂时写死
        stringBuilder.append("00");

        return stringBuilder.toString();
    }

    private SequenceDO selectSequenceDOByName() {
        SequenceDOExample sequenceDOExample = new SequenceDOExample();
        sequenceDOExample.createCriteria().andNameEqualTo("order_info");
        return sequenceDOMapper.selectByExample(sequenceDOExample).get(0);
    }

    private OrderDO convertEntityFromModel(OrderModel orderModel) {
        if (orderModel == null) return null;
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        return orderDO;
    }
}
