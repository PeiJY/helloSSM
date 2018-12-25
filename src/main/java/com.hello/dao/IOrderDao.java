package com.hello.dao;

import com.hello.model.Order;


/**
 * project helloSSM
 * authod wuyanhui
 * datetime 2017/11/23 16:17
 * desc
 */
public interface IOrderDao {
    Order selectOrder(long orderid);
    void insertOrder(Order order);
    void modifyOrder(Order order);
    Order[] findAllAsSeller(long userid);
    Order[] findAllAsBuyer(long userid);
}
