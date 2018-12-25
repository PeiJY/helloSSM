package com.hello.service.impl;

import com.hello.dao.IDommodityDao;
import com.hello.dao.IOrderDao;
import com.hello.dao.IUserDao;
import com.hello.model.Dommodity;
import com.hello.model.Order;
import com.hello.model.User;
import com.hello.service.IOrderService;
import com.hello.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

//import com.hello.dao.impl.UserDaoImpl;

@Service
public class OrderServiceImpl implements IOrderService {



    @Autowired
   private IUserDao userDao;

    @Autowired
    private IDommodityDao dommodityDao;

    @Autowired
    private IOrderDao orderDao;

    @Override
    public long create(long temporaryid, String type, long  dommodityid,long number){
        User user = userDao.findByTemporaryID(temporaryid);
        if(user == null)return -1;//操作者用户不存在或登陆失效
        Dommodity dommodity = dommodityDao.selectDommodity(dommodityid);
        if(dommodity == null)return -2;//没有此物品 }
        if(dommodity.getOwner()==user.getId())return-3;//不能买自己的商品
        long price = dommodity.getPrice();
        long anotheruserid = dommodity.getOwner();
        Order order = new Order(type,dommodityid,user.getId(),anotheruserid,price,number);//String initiator, long dommodityid,long userid,long anotheruserid, long price,long number
        orderDao.insertOrder(order);
        return order.getOrderid();
    }

    @Override
    public int cancel(long temporaryid,  long  orderid){
        User user = userDao.findByTemporaryID(temporaryid);
        if(user == null)return -1;//操作者用户不存在或登陆失效
        Order order = orderDao.selectOrder(orderid);
        if(order == null)return -2;//不存在订单
        if(user.getId()!=order.getBuyerid() && user.getId()!=order.getSellerid())return -3;//订单不属于此用户
        if(!order.getStatus().equals("C"))return -4;//不是正在处理的订单不可取消
        order.cancel();
        orderDao.modifyOrder(order);
        return 1;
    }

    @Override
    public int confirm(long temporaryid,  long  orderid){
        User user = userDao.findByTemporaryID(temporaryid);
        if(user == null)return -1;//操作者用户不存在或登陆失效
        Order order = orderDao.selectOrder(orderid);
        if(order == null)return -2;//不存在订单
        if(user.getId()!=order.getBuyerid() && user.getId()!=order.getSellerid())return -3;//订单不属于此用户
        if(user.getId()==order.getstarter())return -4;//创建者不能确认
        if(order.getStatus().equals("C"))return-5;//取消后不能确认
        order.confirm();
        orderDao.modifyOrder(order);
        return 1;
    }

    @Override
    public Order[] findall(long temporaryid){
        User user = userDao.findByTemporaryID(temporaryid);
        if(user == null)return null;//操作者用户不存在或登陆失效
        Order[] orders1= orderDao.findAllAsSeller(user.getId());
        Order[] orders2= orderDao.findAllAsBuyer(user.getId());
        Order[] orders = new Order[orders1.length+orders2.length];
        if(orders1.length>0) {
            for (int i = 0; i < orders1.length; i++) {
                orders[i] = orders1[i];
            }
        }
        if(orders2.length>0) {
            for (int i = 0; i < orders2.length; i++) {
                orders[i + orders1.length] = orders2[i];
            }
        }
        if(orders.length==0)orders = new Order[] {null};
        return orders;
    }


    @Override
    public int undocancel(long temporaryid,  long  orderid){
        User user = userDao.findByTemporaryID(temporaryid);
        if(user == null)return -1;//操作者用户不存在或登陆失效
        Order order = orderDao.selectOrder(orderid);
        if(order == null)return -2;//不存在订单
        if(user.getId()!=order.getBuyerid() && user.getId()!=order.getSellerid())return -3;//订单不属于此用户
        if(!order.getStatus().equals("C"))return -4;//不是正在处理的订单不可取消
        order.undocancel();
        orderDao.modifyOrder(order);
        return 1;
    }

    @Override
    public Order getorder(long temporaryid,  long  orderid){
        User user = userDao.findByTemporaryID(temporaryid);
        if(user == null)return null;//操作者用户不存在或登陆失效
        Order order = orderDao.selectOrder(orderid);
        if(order == null)return null;//不存在订单
        if(user.getId()!=order.getBuyerid() && user.getId()!=order.getSellerid())return null;//订单不属于此用户
        return order;
    }



}
