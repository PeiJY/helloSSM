package com.hello.service;

import com.hello.model.DommodityTpye;
import com.hello.model.Status;
import com.hello.model.Dommodity;
import com.hello.model.User;

/**
 * date Pei Jiyuan
 * author 2019/4/27
 * desc
 */

public interface IDommodityService {


    public long insertDommodity( String name,
                                 String description, User owner,
                                 Status status,String paytype, DommodityTpye[] type,
                                 String putawayTime, String availableTime,
                                long price, String address,String operation);

    public int statusChange(long id,User user,Status status);

    public int subscribe(long dommodityid, User user, String time);

    public int modityDommodity(String name,
                               String description, User owner,
                               Status status,String paytype, DommodityTpye[] type,
                               String putawayTime, String availableTime,
                               long price, String address,String operation);

    public Object[][] selectAll();

    public Object[][] conditionalSelete(String conditions, String orders, String number);

    public String[] findType(long dommodityid);

    public int report(long dommodityid, User reporter, String reason);


}
