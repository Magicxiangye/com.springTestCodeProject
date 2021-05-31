package com.miaosha.service.impl;

import com.miaosha.dao.UserDOMapper;
import com.miaosha.dao.UserPasswordDOMapper;
import com.miaosha.dataobject.UserDO;
import com.miaosha.dataobject.UserPasswordDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import com.miaosha.validator.ValidationResult;
import com.miaosha.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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

    @Autowired
    private ValidatorImpl validator;

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

        //自定义的校验的类
        ValidationResult result = validator.validate(userModel);
        //当result的hasErr为真的话，就throw异常
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        /*if(StringUtils.isEmpty(userModel.getName())
                || userModel.getGender() == null
                || userModel.getAge() == null
                || StringUtils.isEmpty(userModel.getTelephone())){
            //只要有一个信息是空的，就报错
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }*/

        //信息到数据库
        //userDO的对象连接数据库
        //这样需要把model-》转化为数据库连接使用的dataObject类型
        //使用insertSelective的原因是，当输入的字段为null的时候，是不会去覆盖数据库里原有的默认值的
        UserDO userDO = convertFromModel(userModel);
        //映射给数据库
        //注册过的手机号的唯一值报错catch
        try{
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            //unique的错误抛出
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号重复");
        }

        //密码表的传递
        //相关联的userid要获取
        userModel.setId(userDO.getId());
        //再传值
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;

    }

    //校验登录是否合法
    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        //通过用户的手机号，获取它在数据库中的信息
        //在userDOmapper.xml中先写SQL的方法
        //再在相应mapper的接口中，定义这个方法
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        //判空的准备，查无此人
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        //是真实的存在的用户就拿到相对应的密码的do
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        //组装为usermodel
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);

        //对比两个的密码是否是一样的，相匹配
        //用户输入的密码在vo阶段，先进行加密，到这里直接的进行比较就可以
        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            //不相等，throw
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        //相等的话，直接return
        return userModel;



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
