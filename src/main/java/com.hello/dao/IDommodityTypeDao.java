package com.hello.dao;

import com.hello.model.Dommodity;
import com.hello.model.Subscription;
import org.apache.ibatis.annotations.Param;

/**
 * project helloSSM
 * authod wuyanhui
 * datetime 2017/11/23 16:17
 * desc
 */
public interface IDommodityTypeDao {
    void insertType(@Param("dommodityid")long dommodityid, @Param("type")String type);
    void deleteAllType(long dommodityid);
    String[] findType(long dommodityid);
}
