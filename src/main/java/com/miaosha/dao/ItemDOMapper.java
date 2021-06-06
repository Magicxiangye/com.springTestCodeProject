package com.miaosha.dao;

import com.miaosha.dataobject.ItemDO;
import org.springframework.stereotype.Repository;

/* 以免找不到接口看着难受*/
@Repository
public interface ItemDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO record);

    int insertSelective(ItemDO record);

    ItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemDO record);

    int updateByPrimaryKey(ItemDO record);
}