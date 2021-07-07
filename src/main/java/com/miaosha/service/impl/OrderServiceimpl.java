package com.miaosha.service.impl;

import com.miaosha.dao.OrderDOMapper;
import com.miaosha.dao.SequenceDOMapper;
import com.miaosha.dataobject.OrderDO;
import com.miaosha.dataobject.SequenceDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.ItemService;
import com.miaosha.service.OrderService;
import com.miaosha.service.UserService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.service.model.OrderModel;
import com.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceimpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    //创建订单
    @Override
    //保证创建的订单在同一个事务中
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {
        //1.校验下单的状态
        //商品是否存在，用户是否合法，购买的数量是否合法

        //通过商品的id来获取相应的用户的model
        ItemModel itemModel = itemService.getItemById(itemId);
        //这个model要是获取不到，直接就报错
        if(itemModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }

        //检查用户是否是合法的用户
        UserModel userModel = userService.getUserById(userId);
        //信息不存在，也是直接的报错
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }

        //校验购买的数量是否合法
        if(amount <= 0 || amount > 99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "数量信息不正确");
        }

        //2.落单减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));

        //生成交易的流水号，订单号
        orderModel.setId(generateOrderNo());
        OrderDO orderDO = convertFromOrderModel(orderModel);

        //写入数据库
        orderDOMapper.insertSelective(orderDO);




        return orderModel;
    }


    private OrderDO convertFromOrderModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }

        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        return orderDO;

    }

    //生成订单号的方法
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String generateOrderNo(){
       //一般生成的是十六位的订单号
        StringBuilder stringBuilder = new StringBuilder();

       //前八位为时间的信息，年月日
        LocalDateTime now = LocalDateTime.now();
        //格式的转换
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);


      //中间六位为自增的序列
        //这个订单生成写的不行，之后写过，太拉了
        //序列的初始状态
        int sequence = 0;
        //获取当前的sequence
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");

        sequence = sequenceDO.getCurrentValue();

        //更新数据库里现在的序列的号数
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());

        //更新数据库
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

        //补足六位的订单号
        String sequenceStr = String.valueOf(sequence);
        for(int i = 0;i < sequenceStr.length(); i++){
            stringBuilder.append(0);
        }
        //再接上
        stringBuilder.append(sequenceStr);


      //最后的两位为分库分表位
        //暂时没有这个功能，直接写死00
        stringBuilder.append("00");

        return stringBuilder.toString();
    }
}
