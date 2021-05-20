package com.miaosha.error;

//最后controller出现的错误都会抛出异常Exception,先在这里做处理
//包装器业务异常类实现
public class BusinessException extends Exception implements CommonError{


    //要先关联一个CommonError，就是enum的类
    private CommonError commonError;

    //构造函数
    //直接接收EmBusinessError的传参用于业务异常的构建
    public BusinessException(CommonError commonError){
        //先要调用一下父类Exception的构造方法
        super();
        //再传值
        this.commonError = commonError;
    }

    //接收自定义的errMsg的方式构造业务异常
    public BusinessException(CommonError commonError, String errMsg){
        //先继承
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }


    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
       this.commonError.setErrMsg(errMsg);
       return this;
    }
}
