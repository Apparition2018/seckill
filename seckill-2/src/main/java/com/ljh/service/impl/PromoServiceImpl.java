package com.ljh.service.impl;

import com.ljh.dao.PromoDOMapper;
import com.ljh.entity.PromoDO;
import com.ljh.entity.PromoDOExample;
import com.ljh.service.PromoService;
import com.ljh.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromoServiceImpl implements PromoService {

    private final PromoDOMapper promoDOMapper;

    public PromoServiceImpl(PromoDOMapper promoDOMapper) {
        this.promoDOMapper = promoDOMapper;
    }

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        // 获取对应商品的秒杀活动信息
        PromoDOExample promoDOExample = new PromoDOExample();
        promoDOExample.createCriteria().andItemIdEqualTo(itemId);
        List<PromoDO> promoDoList = promoDOMapper.selectByExample(promoDOExample);
        if (promoDoList.isEmpty()) return null;

        // entity → model
        PromoModel promoModel = this.convertModelFromEntity(promoDoList.get(0));

        // 判断当前时间是否秒杀活动即将开始或正在进行
        if (promoModel.getStartDate().isAfterNow()) {
            promoModel.setStatus(1);
        } else if (promoModel.getEndDate().isBeforeNow()) {
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    private PromoModel convertModelFromEntity(PromoDO promoDO) {
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}
