package com.miaosha.service.impl;

import com.miaosha.dao.UserDOMapper;
import com.miaosha.dao.UserPasswordDOMapper;
import com.miaosha.dataobject.UserDO;
import com.miaosha.dataobject.UserPasswordDO;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//在这里实现继承接口，同时实现接口中的方法
@Service
public class UserServiceImpl implements UserService {
    //重载方法
    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserModel getUserById(Integer id) {
     //根据主键的id获取到主键的对象
        //就是调用到mapper获取到相应的dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        //还是先做判空的处理
        if (userDO == null){
            return null;
        }

        //获取用户的加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);

        return convertFromDataObject(userDO, userPasswordDO);


    }

    //转化为usermodel的方法
    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        //加上两个判空
        if (userDO == null){
            return null;
        }
        //相当于后端的写方法的方式
        UserModel userModel = new UserModel();
        //老方法
        BeanUtils.copyProperties(userDO, userModel);
        if (userPasswordDO != null){
            //把密码单独的传递一下
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }


        return userModel;
    }
}
