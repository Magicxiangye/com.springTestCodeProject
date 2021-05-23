package com.miaosha.controller;


import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//controller中处理异常的基类代码，都放在这里
//子类的controller继承就可以
public class BaseController {


    //声明一个需要的type类型
    public static final String CONTENT_TYPE_FORMED= "application/x-www-form-urlencoded";

    //定义exceptionhandler解决未被controller层吸收的exception
    //所以要接收异常，正确的返回
    @ExceptionHandler(Exception.class)
    //不是服务器端的错误，系统是可以处理的
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        //将要返回的东西先提取到键值对中
        Map<String,Object> responseData = new HashMap<>();
        //先要进行判断是否是BusinessException类型
        if (ex instanceof BusinessException){
            //将接收的异常前置的转换为BusinessException类型
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode",businessException.getErrCode());
            responseData.put("errMsg",businessException.getErrMsg());
        }else {
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg",EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }

        //不加@ResponseBody，返回的前端的路径，加了就可以在前端返回类
        //不管正确还是错误，都是使用的commonReturnType来向前端返回
        return CommonReturnType.create(responseData,"fail");
    }
}
