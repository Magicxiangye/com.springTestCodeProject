package com.miaosha.service.model;

//由于不可以直接将与数据库直连的dataobject传递给前端
//要定义一个model,来与前端的逻辑进行交互
//model才是处理业务逻辑的模型




import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//类似于老的方法里要建立一个form，才可以进行交互
public class UserModel {
    //和数据库的定义的字段一样
    //类型要一致，不然会丢值
    //@声明校验的规则
    private Integer id;
    //定义一下用户名不可以为空，也不可以为null，为空就报message的错误
    @NotBlank(message = "用户名不可以为空")
    private String name;

    //性别是不能为null
    @NotNull(message = "性别不能不填写")
    private Integer gender;

    @NotNull(message = "年龄不能不填写")
    @Min(value = 0,message = "年龄必须大于0")
    @Max(value = 150, message = "年龄必须小于150")
    private Integer age;

    @NotBlank(message = "手机号不可以为空")
    private String telephone;

    private String registerMode;
    private String thirdPartyId;
    //密码在另一个表里
    @NotBlank(message = "密码不可以为空")
    private String encrptPassword;

    public String getEncrptPassword() {
        return encrptPassword;
    }

    public void setEncrptPassword(String encrptPassword) {
        this.encrptPassword = encrptPassword;
    }

    //快速的生成get,set方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRegisterMode() {
        return registerMode;
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }
}
