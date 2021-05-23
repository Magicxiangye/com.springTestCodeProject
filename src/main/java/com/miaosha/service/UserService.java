package com.miaosha.service;

import com.miaosha.error.BusinessException;
import com.miaosha.service.model.UserModel;

//userService的接口
public interface UserService {
    //定义接口的方法
    //通过用户的id获取用户对象的方法
    //所以方法应该返回的是usermodel的对象
    UserModel getUserById(Integer id);

    //用来处理用户注册请求的方法
    void register(UserModel userModel) throws BusinessException;
}
