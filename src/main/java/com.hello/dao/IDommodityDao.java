package com.hello.dao;

import com.hello.model.Dommodity;
import com.hello.model.Report;
import com.hello.model.Subscription;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * authod Pei Jiyuan
 * datetime 2019/4/27
 * desc
 */


public interface IDommodityDao {
    Dommodity selectDommodity(long id);
    void insertDommodity(Dommodity dommodity);
    Dommodity findByName(String name);
    void modifyDommodity(Dommodity dommodity);
    void subscribe(Subscription sub);
    Dommodity[] selectAll(long id);
    Dommodity[] conditionalSelete(@Param("conditions") String conditions,@Param("orders") String orders ,@Param("number") String number);
    void report(Report report);
}
