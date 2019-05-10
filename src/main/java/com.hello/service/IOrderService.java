package com.hello.service;

import com.hello.model.Order;
/**
 * authod Pei Jiyuan
 * datetime 2019/4/27
 * desc
 */

public interface IOrderService {

    public long create(long temporaryid, String type, long  dommodityid,long number);

    public int cancel(long temporaryid,  long  orderid);

    public int undocancel(long temporaryid,  long  orderid);

    public int confirm(long temporaryid,  long  orderid);

    public Order[] findall(long temporaryid);

    public Order getorder(long temporaryid,  long  orderid);


}
