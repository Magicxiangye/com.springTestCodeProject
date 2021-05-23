package com.miaosha.service.impl;

import com.miaosha.dao.UserDOMapper;
import com.miaosha.dao.UserPasswordDOMapper;
import com.miaosha.dataobject.UserDO;
import com.miaosha.dataobject.UserPasswordDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        //还是先判空
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //信息的校验需要在pom里加上相应的pom包，org.apache.commons
        if(StringUtils.isEmpty(userModel.getName())
                || userModel.getGender() == null
                || userModel.getAge() == null
                || StringUtils.isEmpty(userModel.getTelephone())){
            //只要有一个信息是空的，就报错
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //信息到数据库
        //userDO的对象连接数据库
        //这样需要把model-》转化为数据库连接使用的dataObject类型
        //使用insertSelective的原因是，当输入的字段为null的时候，是不会去覆盖数据库里原有的默认值的
        UserDO userDO = convertFromModel(userModel);
        //映射给数据库
        userDOMapper.insertSelective(userDO);

        //密码表的传递
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;

    }

    //还有一个密码do的转化方式
    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        //不空的时候
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());

        return userPasswordDO;
    }
    //model再转化为userdo的方法
    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        //赋值
        BeanUtils.copyProperties(userModel, userDO);

        return userDO;
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
