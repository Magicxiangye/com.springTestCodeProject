package com.miaosha.service;

import com.miaosha.service.model.UserModel;

//userService的接口
public interface UserService {
    //定义接口的方法
    //通过用户的id获取用户对象的方法
    //所以方法应该返回的是usermodel的对象
    UserModel getUserById(Integer id);
}
