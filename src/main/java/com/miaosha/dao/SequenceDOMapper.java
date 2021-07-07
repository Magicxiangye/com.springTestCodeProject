package com.miaosha.dao;

import com.miaosha.dataobject.SequenceDO;
import org.springframework.stereotype.Repository;

/* 以免找不到接口看着难受*/
@Repository
public interface SequenceDOMapper {
    int deleteByPrimaryKey(String name);

    int insert(SequenceDO record);

    int insertSelective(SequenceDO record);

    SequenceDO selectByPrimaryKey(String name);

    SequenceDO getSequenceByName(String name);

    int updateByPrimaryKeySelective(SequenceDO record);

    int updateByPrimaryKey(SequenceDO record);
}