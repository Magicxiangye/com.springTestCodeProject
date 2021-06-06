package com.miaosha.controller;

import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.miaosha.controller.viewobject.ItemVO;
import com.miaosha.error.BusinessException;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.ItemService;
import com.miaosha.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ItemController extends BaseController{


    @Autowired
    private ItemService itemService;

    //创建商品的Controller
    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock")Integer stock,
                                       @RequestParam(name = "imgUrl")String imgUrl) throws BusinessException {

        //封装service请求来创建商品
        //尽量让service复杂，controller层简单

        //构建itemmodel的请求
        ItemModel itemModel = new ItemModel();
        //简单的传值
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);

        //调用创建的方法，拿到相应的返回值
        ItemModel itemModelFromReturn = itemService.createItem(itemModel);

        //使用的是VO，将信息返回到前端
        ItemVO itemVO = convertVOFromModel(itemModelFromReturn);

        return CommonReturnType.create(itemVO);

    }

    //商品详情页面的浏览的功能
    //创建商品的Controller
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id")Integer id){
        //通过getId的方式来获得
        ItemModel itemModel = itemService.getItemById(id);

        ItemVO itemVO = convertVOFromModel(itemModel);

        return CommonReturnType.create(itemVO);


    }



    private ItemVO convertVOFromModel(ItemModel itemModel){
        //先判空
        if (itemModel == null) {
            return null;
        }

        //赋值转化一下
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);

        //返回
        return itemVO;

    }

}
