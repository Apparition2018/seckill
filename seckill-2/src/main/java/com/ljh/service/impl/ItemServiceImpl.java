package com.ljh.service.impl;

import com.ljh.dao.ItemDOMapper;
import com.ljh.dao.ItemStockDOMapper;
import com.ljh.entity.ItemDO;
import com.ljh.entity.ItemDOExample;
import com.ljh.entity.ItemStockDO;
import com.ljh.entity.ItemStockDOExample;
import com.ljh.error.BusinessException;
import com.ljh.error.BusinessErrorEnum;
import com.ljh.service.ItemService;
import com.ljh.service.PromoService;
import com.ljh.service.model.ItemModel;
import com.ljh.service.model.PromoModel;
import com.ljh.validator.ValidationResult;
import com.ljh.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ValidatorImpl validator;
    private final ItemDOMapper itemDOMapper;
    private final ItemStockDOMapper itemStockDOMapper;
    private final PromoService promoService;

    public ItemServiceImpl(ValidatorImpl validator, ItemDOMapper itemDOMapper, ItemStockDOMapper itemStockDOMapper, PromoService promoService) {
        this.validator = validator;
        this.itemDOMapper = itemDOMapper;
        this.itemStockDOMapper = itemStockDOMapper;
        this.promoService = promoService;
    }

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        // 校验入参
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasErrors())
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR);

        // model → entity
        ItemDO itemDO = this.convertEntityFromModel(itemModel);

        // 写入数据库
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = this.convertItemStockDOFromModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);

        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList = this.listOrderBySalesDesc();
        return itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = this.selectStockDOByItSemId(itemDO.getId());
            return this.convertModelFromEntity(itemDO, itemStockDO);
        }).collect(Collectors.toList());
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO == null) return null;
        ItemStockDO itemStockDO = this.selectStockDOByItSemId(itemDO.getId());

        // model → entity
        ItemModel itemModel = this.convertModelFromEntity(itemDO, itemStockDO);

        // 获取活动商品信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if (promoModel != null && promoModel.getStatus() != 3)
            itemModel.setPromoModel(promoModel);

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        return itemStockDOMapper.decreaseStock(itemId, amount) > 0;
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) {
        itemDOMapper.increaseSales(itemId, amount);
    }

    private List<ItemDO> listOrderBySalesDesc() {
        ItemDOExample itemDOExample = new ItemDOExample();
        itemDOExample.createCriteria();
        itemDOExample.setOrderByClause("sales DESC");
        return itemDOMapper.selectByExample(itemDOExample);
    }

    private ItemStockDO selectStockDOByItSemId(Integer itemId) {
        ItemStockDOExample itemStockDOExample = new ItemStockDOExample();
        itemStockDOExample.createCriteria().andItemIdEqualTo(itemId);
        return itemStockDOMapper.selectByExample(itemStockDOExample).get(0);
    }

    private ItemDO convertEntityFromModel(ItemModel itemModel) {
        if (itemModel == null) return null;
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);
        return itemDO;
    }

    private ItemStockDO convertItemStockDOFromModel(ItemModel itemModel) {
        if (itemModel == null) return null;
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

    private ItemModel convertModelFromEntity(ItemDO itemDO, ItemStockDO itemStockDO) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}