package com.miaosha.service;


import com.miaosha.service.model.OrderModel;

//处理订单交易的service
public interface OrderService {

    //创建交易的方法
    OrderModel createOrder(Integer userId, Integer itemId,Integer amount);

}
