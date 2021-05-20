package com.miaosha.dao;

import com.miaosha.dataobject.UserPasswordDO;
import org.springframework.stereotype.Repository;

/* 以免找不到接口看着难受*/
@Repository
public interface UserPasswordDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPasswordDO record);

    int insertSelective(UserPasswordDO record);

    UserPasswordDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPasswordDO record);

    int updateByPrimaryKey(UserPasswordDO record);

    //新建在DoMapper.xml文件里设置的数据库查询方法的java方法
    //以便于使用
    UserPasswordDO selectByUserId(Integer userId);
}