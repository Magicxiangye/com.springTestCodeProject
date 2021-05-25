package com.miaosha.dao;

import com.miaosha.dataobject.UserDO;
import org.springframework.stereotype.Repository;
/* 以免找不到接口看着难受*/
@Repository
public interface UserDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    UserDO selectByPrimaryKey(Integer id);

    //通过手机号查询的映射
    UserDO selectByTelphone(String telphone);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);
}