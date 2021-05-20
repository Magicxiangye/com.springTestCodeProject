package com.miaosha.error;

public interface CommonError {
    //定义接口的三个方法

    public int getErrCode();
    public String getErrMsg();

    public CommonError setErrMsg(String errMsg);
}
