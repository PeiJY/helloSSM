package com.hello.service;

import com.hello.model.DommodityTpye;
import com.hello.model.Status;
import com.hello.model.Dommodity;

/**
 * authod Pei Jiyuan
 * datetime 2019/4/27
 * desc
 */

public interface IDommodityService {


    public long insertDommodity( String name,
                                 String description, long temporaryid,
                                 Status status,String paytype, DommodityTpye[] type,
                                 String putawayTime, String availableTime,
                                long price, String address,String operation);

    public int statusChange(long id,long temporaryid,Status status);

    public int subscribe(long dommodityid, long temporaryid, String time);

    public int modityDommodity(String name,
                               String description, long temporaryid,
                               Status status,String paytype, DommodityTpye[] type,
                               String putawayTime, String availableTime,
                               long price, String address,String operation);

    public Object[][] selectAll();

    public Object[][] conditionalSelete(String conditions, String orders, String number);

    public String[] findType(long dommodityid);

    public int report(long dommodityid,long temporaryid,String reason);


}
