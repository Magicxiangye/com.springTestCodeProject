package com.miaosha.service.impl;

import com.miaosha.dao.ItemDOMapper;
import com.miaosha.dao.ItemStockDOMapper;
import com.miaosha.dataobject.ItemDO;
import com.miaosha.dataobject.ItemStockDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.ItemService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.validator.ValidationResult;
import com.miaosha.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    //连接数据库的mapper
    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    //类型转换的方法
    //将itemmodel转化为dataobject
    private ItemDO convertItemDOFromItemModel(ItemModel itemModel){
        //先做一个判空的处理
        if(itemModel == null){
            return null;
        }

        //先实例化一个对象
        ItemDO itemDO = new ItemDO();
        //copy function
        BeanUtils.copyProperties(itemModel, itemDO);
        //itemmodel的价格的类型为BigDecimal，数据库的类型为double
        //bean不会复制类型不一样的数据，所以价格要手动的赋值
        //转变为数据库的double类型
        itemDO.setPrice(itemModel.getPrice().doubleValue());

        //返回对象
        return itemDO;
    }

    //转换为库存对象的convert-function
    private ItemStockDO convertItemStockFromItemModel(ItemModel itemModel){
        //还是先做的判空的处理
        if(itemModel == null){
            return null;
        }
        //实例化一个要转化的对象
        ItemStockDO itemStockDO = new ItemStockDO();
        //复制
        //商品的id
        itemStockDO.setItemId(itemModel.getId());
        //库存
        itemStockDO.setStock(itemModel.getStock());

        return itemStockDO;
    }


    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult validationResult = validator.validate(itemModel);
        if(validationResult.isHasErrors()){
            //有错的话，直接throw一个报错
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }


        //转化itemmodel->dataobject
        //使用自定义的转换函数进行转换
        ItemDO itemDO = this.convertItemDOFromItemModel(itemModel);

        //写入数据库
        itemDOMapper.insertSelective(itemDO);
        //写入数据库成功后，要拿到item的id号
        itemModel.setId(itemDO.getId());

        //转换库存表的数据，进行插入
        ItemStockDO itemStockDO = this.convertItemStockFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);

        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        return null;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        //现在需要的是do要转化为model
        ItemDO itemDo = itemDOMapper.selectByPrimaryKey(id);
        //先判断一下有没有这一条的商品数据
        if(itemDo == null){
            return null;
        }
        //操作获得商品库存的数量
       ItemStockDO itemStockDO =  itemStockDOMapper.selectByItemId(itemDo.getId());

        //类型进行转化
        ItemModel itemModel = convertItemModelFromDataObject(itemDo, itemStockDO);

        //返回
        return itemModel;
    }

    //反向的转化
    //dataObject-->转化为model

    private ItemModel convertItemModelFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO){
        //两个do的组合变为model
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        //手动合为一个对象
        //两个类型不一样的也要自动赋值
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());

        return itemModel;

    }


}
