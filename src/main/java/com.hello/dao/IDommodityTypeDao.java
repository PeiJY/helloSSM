package com.hello.dao;

import com.hello.model.Dommodity;
import com.hello.model.Subscription;
import org.apache.ibatis.annotations.Param;

/**
 * author Pei Jiyuan
 * date 2019/4/27
 * desc
 */

public interface IDommodityTypeDao {
    void insertType(@Param("dommodityid")long dommodityid, @Param("type")String type);
    void deleteAllType(long dommodityid);
    String[] findType(long dommodityid);
}
