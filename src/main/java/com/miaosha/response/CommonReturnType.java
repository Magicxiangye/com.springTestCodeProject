package com.miaosha.response;


//这个是通用的正确形式的固定的返回格式
public class CommonReturnType {

    //两个信息组成
    //提示信息和数据的对象
    //返回的是success或者fail
    private String status;
    //status=success,data返回前端需要的json数据
    //status=fail,data返回的是公用的错误代码格式哦
    private Object data;

    //定义一个通用的创建方法
    public static CommonReturnType create(Object result){
        //只返回result的话，status默认为success
        return CommonReturnType.create(result,"success");
    }
    //重载这个方法
    public static CommonReturnType create(Object result, String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
