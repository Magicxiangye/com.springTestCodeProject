package com.miaosha.error;

//enum是定义的是编译后的一个java的枚举类
public enum EmBusinessError implements CommonError{
    //定义错误码
    //定义通用的错误类型10001
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    //定义一个未知的错误
    UNKNOWN_ERROR(10002, "未知错误"),
    //20000开头的就是用户信息的相关性错误，全局的错误码生成
    USER_NOT_EXIST(20001, "用户不存在"),
    USER_LOGIN_FAIL(20002,"用户手机号或密码不正确"),


    //30000开头为交易信息的错误
    STOCK_NOT_ENOUGH(30001, "库存不足")
    ;
    //;这个号上边定义的是枚举类的相应的错误的定义代码，当符合类型的时候
    //就会返回相应的错误码
    //先定义一个构造函数
    private EmBusinessError(int errCode, String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
