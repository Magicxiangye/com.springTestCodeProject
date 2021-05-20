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

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);
}