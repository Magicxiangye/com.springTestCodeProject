package com.miaosha.controller;

import com.miaosha.controller.viewobject.UserVO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* 指定一个controller，名字为user*/
@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController{
    //调用一下用户的服务
    @Autowired
    private UserService userService;

    //获取用户的方法
    //接收的事requestparam的用户id的参数
    @RequestMapping("/get")
    @ResponseBody
    //所以真正的返回的信息，就要变为vo
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        //调用service服务获取到对应id的服务的对象并返回给前端
        //获取了之后传递给model,再把model返回出去
        UserModel userModel = userService.getUserById(id);
        //若获取对应的用户信息不存在
        if(userModel == null){
            //抛出的是自定义的异常反馈
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        //返回的是viewobjectVO的对象给UI使用
       UserVO userVO = convertFromModel(userModel);

       //返回的是通用的对象
       return CommonReturnType.create(userVO);

    }


    //设置转换的方法，还是通过bean来进行传递
    private UserVO convertFromModel(UserModel userModel){
        //先判空
        if (userModel == null){
             return null;
        }
        UserVO userVO = new UserVO();
        //bean传递
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }



}
