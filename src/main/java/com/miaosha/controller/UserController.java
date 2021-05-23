package com.miaosha.controller;

import com.miaosha.controller.viewobject.UserVO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/* 指定一个controller，名字为user*/
//CrossOrigin,解决的是ajax跨域请求不安全的前端数据无法获得的错误
@Controller("user")
@RequestMapping("/user")
@CrossOrigin
public class UserController extends BaseController{
    //调用一下用户的服务
    @Autowired
    private UserService userService;

    @Autowired
    //bean找不到，不管了，傻逼springMVC
    private HttpServletRequest httpServletRequest;

    //用户注册的接口
    //要用户的注册手机号，以及验证码
    //以及用户注册需要的信息
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telphone")String telphone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name="age")Integer age,
                                     @RequestParam(name="password")String password) throws BusinessException {

        //第一步是验证手机号和相应的otp的code的符合性
        //从httpsession中将手机号和验证码取出配对
        String inSessionOtpCode = (String) httpServletRequest.getSession().getAttribute(telphone);
        //德鲁伊池的比较,这是有判空处理的equals
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            //不相等直接的抛出异常
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不正确");
        }
        //合法的话就进入用户的注册的流程
        //用户的注册流程
        UserModel userModel = new UserModel();
        //设置值
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelephone(telphone);
        userModel.setRegisterMode("byphone");
        //MD5的数据加密的方式来存储用户的密码
        userModel.setEncrptPassword(MD5Encoder.encode(password.getBytes()));

        userService.register(userModel);

        return CommonReturnType.create(null);


    }


    //项目使用的第一步就是要有一个用户获取短信页面的注册接口
    //监听的Mapping
    //post的标注是为了，本方法必须要是监听到了前端的post方法才可以生效
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telphone")String telphone){
        //给用户发送验证码的流程

        //需要按照一定的规则生成OTP验证码
        //这里使用的是随机数的生成来当作otp的验证码
        Random random = new Random();
        //验证码的最大值
        //以后还是设置相同位数的验证码更好
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        //类型的转换，int转换为string
        String otpCode = String.valueOf(randomInt);
        //将OTP的验证码同相应的用户相关联
        //使用的是httpseesion的方式绑定他的手机号以及OTPCODE
        httpServletRequest.getSession().setAttribute(telphone, otpCode);

        //将OTP验证码通过短信通道发送给用户（没有买省略）
        System.out.println("telphone=" + telphone + "&otpCode=" + otpCode );


        return CommonReturnType.create(null);

    }

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
