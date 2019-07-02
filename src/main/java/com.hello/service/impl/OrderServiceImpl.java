package com.hello.service.impl;

import com.hello.dao.IDommodityDao;
import com.hello.dao.IOrderDao;
import com.hello.dao.IUserDao;
import com.hello.model.Dommodity;
import com.hello.model.Order;
import com.hello.model.User;
import com.hello.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author Pei Jiyuan
 * date 2019/4/27
 * desc
 */

@Service
public class OrderServiceImpl implements IOrderService {



    @Autowired
   private IUserDao userDao;

    @Autowired
    private IDommodityDao dommodityDao;

    @Autowired
    private IOrderDao orderDao;

    @Override
    public int create(User user, String type, long  dommodityid,long number){
        Dommodity dommodity = dommodityDao.selectDommodity(dommodityid);
        if(dommodity == null)return -2;//没有此物品
        if(dommodity.getOwner() == user.getId())return-3;//不能买自己的商品
        long price = dommodity.getPrice();
        long anotheruserid = dommodity.getOwner();
        Order order = new Order(type,dommodityid,user.getId(),anotheruserid,price,number);//String initiator, long dommodityid,long userid,long anotheruserid, long price,long number
        orderDao.insertOrder(order);
        return (int)order.getOrderid();
    }

    @Override
    public int cancel(User user,  long  orderid){
        Order order = orderDao.selectOrder(orderid);
        if(order == null)return -2;//不存在订单
        if(user.getId()!=order.getBuyerid() && user.getId()!=order.getSellerid())return -3;//订单不属于此用户
        if(!order.getStatus().equals("C"))return -4;//不是正在处理的订单不可取消
        order.cancel();
        orderDao.modifyOrder(order);
        return 0;
    }

    @Override
    public int confirm(User user,  long  orderid){
        Order order = orderDao.selectOrder(orderid);
        if(order == null)return -2;//不存在订单
        if(user.getId()!=order.getBuyerid() && user.getId()!=order.getSellerid())return -3;//订单不属于此用户
        if(user.getId()==order.getstarter())return -4;//创建者不能确认
        if(order.getStatus().equals("C"))return-5;//取消后不能确认
        order.confirm();
        orderDao.modifyOrder(order);
        return 0;
    }

    @Override
    public Order[] findall(User user){
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
    public int undocancel(User user,  long  orderid){
        Order order = orderDao.selectOrder(orderid);
        if(order == null)return -2;//不存在订单
        if(user.getId()!=order.getBuyerid() && user.getId()!=order.getSellerid())return -3;//订单不属于此用户
        if(!order.getStatus().equals("C"))return -4;//不是正在处理的订单不可取消
        order.undocancel();
        orderDao.modifyOrder(order);
        return 0;
    }

    @Override
    public Order getorder(User user,  long  orderid){
        Order order = orderDao.selectOrder(orderid);
        if(order == null)return null;//不存在订单
        if(user.getId() != order.getBuyerid() && user.getId() != order.getSellerid()){
            order.setBuyerid(-1);
            return order;//订单不属于此用户
        }
        return order;
    }



}
