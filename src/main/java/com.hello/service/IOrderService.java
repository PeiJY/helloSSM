package com.hello.service;

import com.hello.model.Order;
import com.hello.model.User;

/**
 * date Pei Jiyuan
 * author 2019/4/27
 * desc
 */

public interface IOrderService {

    public int create(User user, String type, long  dommodityid, long number);

    public int cancel(User user,  long  orderid);

    public int undocancel(User user,  long  orderid);

    public int confirm(User user,  long  orderid);

    public Order[] findall(User user);

    public Order getorder(User user,  long  orderid);


}
