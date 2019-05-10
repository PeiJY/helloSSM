package com.hello.dao;

import com.hello.model.Order;


/**
 * authod Pei Jiyuan
 * datetime 2019/4/27
 * desc
 */

public interface IOrderDao {
    Order selectOrder(long orderid);
    void insertOrder(Order order);
    void modifyOrder(Order order);
    Order[] findAllAsSeller(long userid);
    Order[] findAllAsBuyer(long userid);
}
