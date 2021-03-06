package com.miaosha.service.model;

//先设计相应的模型再设计数据库

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemModel {
    private Integer id;

    //商品名
    @NotBlank(message = "商品名不能不填")
    private String title;

    //商品的价格
    @NotNull(message = "商品的价格不能为")
    @Min(value = 0,message = "商品必须大于零")
    private BigDecimal price;

    //库存
    @NotNull(message = "库存不能为空")
    private Integer stock;

    //描述
    @NotBlank(message = "描述不可以为空")
    private String description;

    //商品的销量
    private Integer sales;

    //商品的图片
    @NotBlank(message = "图片的信息不可以为空")
    private String imgUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
