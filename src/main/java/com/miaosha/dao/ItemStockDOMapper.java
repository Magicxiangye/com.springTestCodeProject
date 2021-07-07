package com.miaosha.dao;

import com.miaosha.dataobject.ItemStockDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/* 以免找不到接口看着难受*/
@Repository
public interface ItemStockDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemStockDO record);

    int insertSelective(ItemStockDO record);

    ItemStockDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemStockDO record);

    int updateByPrimaryKey(ItemStockDO record);

    //通过商品的id来查找库存

    ItemStockDO selectByItemId(Integer itemId);

    int decreaseStock(@Param("itemId") Integer itemId, @Param("amount") Integer amount);
}