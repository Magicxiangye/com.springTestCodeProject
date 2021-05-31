package com.miaosha.validator;


import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

//获取到hibernate的校验的结果
//再传递给前端的应用程序
//这个类在应用程序以及hibernate之间做了对接
public class ValidationResult {
    //校验各国是否有错
    private boolean hasErrors = false;

    //存放错误信息的map
    //附初始值以免有空指针的错误
    private Map<String, String> errorMsgMap = new HashMap<>();

    //下面都是get,set方法


    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    //实现通用的通过格式化字符串信息获取错误结果的msg方法
    public String getErrMsg(){
        //可能有多个字段出现问题
        //先要toArray(),再用连接，使用逗号分割开
        return StringUtils.join(errorMsgMap.values().toArray(), ",");
    }

}
