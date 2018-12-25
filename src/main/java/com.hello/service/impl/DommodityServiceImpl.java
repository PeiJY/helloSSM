package com.hello.service.impl;

import com.hello.dao.IDommodityDao;
import com.hello.dao.IDommodityTypeDao;
import com.hello.dao.IPictureDao;
import com.hello.dao.IUserDao;
import com.hello.model.*;
import com.hello.service.IDommodityService;
import com.hello.service.IPictureService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


@Service
public class DommodityServiceImpl implements IDommodityService {



    @Autowired
   private IDommodityDao dommodityrDao;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IDommodityTypeDao dommodityTypeDao;
    @Autowired
    private IPictureDao pictureDao;

    @Override
    public long insertDommodity(String name,
                                String description, long temporaryid,
                                Status status,String paytype, DommodityTpye[] type,
                                String putawayTime, String availableTime,
                               long price, String address,String operation){
        User owner = userDao.findByTemporaryID(temporaryid);
        if(owner == null)return -1;//操作者用户不存在或登陆失效
        long ownerid = owner.getId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        Date dputawayTime = null;
        Date davailableTime = null;
        try {
            dputawayTime = sdf.parse(putawayTime);
            davailableTime = sdf.parse(availableTime);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        Dommodity dommodity = new Dommodity(2,name,description,ownerid,status,paytype,dputawayTime,davailableTime,operation);
        if(dommodity==null)return -2;//商品信息有误
        System.out.println(dommodity.toString());
        dommodity.setPrice(price);
        dommodity.setAddress(address);
        dommodityrDao.insertDommodity(dommodity);
        long id = dommodity.getId();
        System.out.println(dommodity.getId());
        for (DommodityTpye a:type) {
            dommodityTypeDao.insertType(dommodity.getId(),a.toString());
        }
        return id;
    }


    @Override
    public String[] findType(long dommodityid){
        return dommodityTypeDao.findType(dommodityid);
    }

    @Override
    public Object[][] selectAll(){
        Dommodity[] all = dommodityrDao.selectAll(1);
        Object[][] ud = new Object[all.length][3];
        for(int i=0;i<all.length;i++){
            ud[i][0] = all[i];
            ud[i][1] = userDao.selectUser(all[i].getOwner());
        }
        return ud;
    }

    @Override
    public Object[][] conditionalSelete(String conditions, String orders , String number){
        Dommodity[] all = dommodityrDao.conditionalSelete(conditions,orders,number);
        Object[][] ud = new Object[all.length][2];
        for(int i=0;i<all.length;i++){
            ud[i][0] = all[i];
            ud[i][1] = userDao.selectUser(all[i].getOwner());
        }
        return ud;
    }

    @Override
    public int report(long dommodityid,long temporaryid,String reason){
        User reporter = userDao.findByTemporaryID(temporaryid);
        if(reporter == null)return -1;//操作者用户不存在或登陆失效
        Dommodity dommodity = dommodityrDao.selectDommodity(dommodityid);
        if(dommodity == null) return -2;//m没有此物品
        long reporterid = reporter.getId();
        String reportName= reporter.getUsername();
        Report report = new Report(reason,dommodityid,reporterid);
        dommodityrDao.report(report);
        return 1;
    }

    @Override
    public int modityDommodity(String name,
                               String description, long temporaryid,
                               Status status,String paytype, DommodityTpye[] type,
                               String putawayTime, String availableTime,
                               long price, String address,String operation){
        User owner = userDao.findByTemporaryID(temporaryid);
        if(owner == null)return -1;//操作者用户不存在或登陆失效
        long ownerid = owner.getId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        Date dputawayTime = null;
        Date davailableTime = null;
        try {
            dputawayTime = sdf.parse(putawayTime);
            davailableTime = sdf.parse(availableTime);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        Dommodity newdommodity = new Dommodity(2,name,description,ownerid,status,paytype,dputawayTime,davailableTime,operation);
        Dommodity dommodity = dommodityrDao.findByName(name);
        if(dommodity==null)return -2;//商品不存在
        long id = dommodity.getId();
        newdommodity.setAddress(address);
        newdommodity.setPrice(price);
        newdommodity.setId(id);
        dommodityrDao.modifyDommodity(newdommodity);
        dommodityTypeDao.deleteAllType(dommodity.getId());
        for (DommodityTpye a:type) {
            dommodityTypeDao.insertType(dommodity.getId(),a.toString());
        }
        System.out.println(dommodity.toString());
        return 0;
    }

    @Override
    public int subscribe(long dommodityid, long temporaryid, String time){
        User user = userDao.findByTemporaryID(temporaryid);
        if(user == null){
            return -1;//没有此用户，或未登录
        }
        long userid = user.getId();
        Dommodity dommodity = dommodityrDao.selectDommodity(dommodityid);
        if(dommodity == null){
            return -2;//m没有此物品
        }
        Subscription sub = new Subscription(userid,dommodityid,time);
        dommodityrDao.subscribe(sub);
        return 0;
    }

    @Override
    public int statusChange(long id,long temporaryid,Status status){
        User user = userDao.findByTemporaryID(temporaryid);
        if(user == null){
            return -1;//没有此用户，或未登录
        }
        long userid = user.getId();
        Dommodity dommodity = dommodityrDao.selectDommodity(id);
        if(dommodity == null){
            return -2;//m没有此物品
        }
        if(dommodity.getOwner()!=userid){
            return -3;//物品不属于此用户，无权下架
        }
        dommodity.setStatus(status);
        dommodityrDao.modifyDommodity(dommodity);
        return 0;
    }


}
