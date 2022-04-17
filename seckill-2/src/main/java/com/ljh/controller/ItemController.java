package com.ljh.controller;

import com.ljh.controller.vo.ItemVO;
import com.ljh.error.BusinessException;
import com.ljh.response.CommonReturnType;
import com.ljh.service.ItemService;
import com.ljh.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController("/item")
@RequestMapping("/item")
public class ItemController extends BaseController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * 创建商品
     */
    @PostMapping("/create")
    public CommonReturnType createItem(@RequestParam(name = "title") String title, @RequestParam(name = "description") String description,
                                       @RequestParam(name = "price") BigDecimal price, @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModelFormReturn = itemService.createItem(itemModel);
        ItemVO itemVO = this.convertVOFromModel(itemModelFormReturn);
        return CommonReturnType.create(itemVO);
    }

    /**
     * 商品详情页浏览
     * http://localhost:6002/item/get?id=1
     */
    @GetMapping("/get")
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id) {
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = this.convertVOFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    /**
     * 商品列表页面浏览
     * http://localhost:6002/item/list
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public CommonReturnType listItem() {
        List<ItemModel> itemModelList = itemService.listItem();
        List<ItemVO> itemVOList = itemModelList.stream().map(this::convertVOFromModel).collect(Collectors.toList());
        return CommonReturnType.create(itemVOList);
    }

    private ItemVO convertVOFromModel(ItemModel itemModel) {
        if (itemModel == null) return null;
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        if (itemModel.getPromoModel() != null) {
            // 有正在进行或即将进行的秒杀活动
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }
}
