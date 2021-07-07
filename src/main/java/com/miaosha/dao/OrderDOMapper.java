package com.miaosha.dao;

import com.miaosha.dataobject.OrderDO;
import org.springframework.stereotype.Repository;

/* 以免找不到接口看着难受*/
@Repository
public interface OrderDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderDO record);

    int insertSelective(OrderDO record);

    OrderDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderDO record);

    int updateByPrimaryKey(OrderDO record);
}