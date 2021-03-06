package com.miaosha.service;

//商品功能的接口

import com.miaosha.error.BusinessException;
import com.miaosha.service.model.ItemModel;

import java.util.List;


public interface ItemService {

    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表浏览
     List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);

    //库存扣减的方法
    boolean decreaseStock(Integer itemId, Integer amount)throws BusinessException;

}
