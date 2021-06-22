package com.miaosha.service.impl;

import com.miaosha.service.OrderService;
import com.miaosha.service.model.OrderModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceimpl implements OrderService {

    //创建订单
    @Override
    //保证创建的订单在同一个事务中
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) {
        //校验下单的状态
        //商品是否存在，用户是否合法，购买的数量是否合法


        //落单减库存，支付减库存


        return null;
    }
}
