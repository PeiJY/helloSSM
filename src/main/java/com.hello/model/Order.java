package com.hello.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time;

/**
 * authod Pei Jiyuan
 * datetime 2019/4/27
 * desc
 */

public class Order {
    private long orderid;
    private Date starttime;
    private Date endtime;
    private String initiator;
    private long buyerid;
    private long sellerid;
    private long dommodityid;
    private long number;
    private long price;
    private String status;


    @Override
    public String toString() {
        return "Order{" +
                "orderid=" + String.valueOf(orderid) +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", initiator='" + initiator + '\'' +
                ", buyerid='" + String.valueOf(buyerid) + '\'' +
                ",sellerid= '"+ String.valueOf(sellerid)+"\'"+
                ",dommodityid= '"+ String.valueOf(dommodityid)+"\'"+
                ",number= '"+ String.valueOf(number)+"\'"+
                ",price= '"+ String.valueOf(price)+"\'"+
                ",status= '"+ status+"\'"+
                '}';
    }

    public String returnMassage(String buyername,String sellername){
        return "{"+
                "\"orderid\":\"" + String.valueOf(orderid) + '\"' +
                "\"dommodityid\":\"" + String.valueOf(dommodityid) + '\"' +
                ",\"starttime\":\"" + starttime + '\"' +
                ",\"endtime\":\"" + endtime + '\"' +
                ",\"initiator\":\"" + initiator + '\"' +
                ",\"buyername\":\"" + buyername + '\"' +
                ",\"sellername\":\"" + sellername + '\"' +
                ",\"number\":\"" + String.valueOf(number) + '\"' +
                ",\"price\":\"" + String.valueOf(price) + '\"' +
                ",\"status\":\"" + status + '\"' +
                ",\"status\":\""+ String.valueOf(dommodityid)+"\""+"}";
    }
    public Order(){
        starttime = new Date();
        this.status = "P";
    }

    public Order(String initiator, long dommodityid,long userid,long anotheruserid, long price,long number) {
        starttime = new Date();
        this.initiator = initiator;
        this.number = number;
        this.dommodityid = dommodityid;
        this.price = price;
        if(initiator.equals("B")){
            this.buyerid = userid;
            this.sellerid = anotheruserid;
        }
        else if(initiator.equals("S")){
            this.sellerid = userid;
            this.buyerid = anotheruserid;
        }
        this.status = "P";
    }

    public long getstarter(){
        if(initiator.equals("B"))return buyerid;
        else return sellerid;
    }


    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public long getBuyerid() {
        return buyerid;
    }

    public void setBuyerid(long buyerid) {
        this.buyerid = buyerid;
    }

    public long getSellerid() {
        return sellerid;
    }

    public void setSellerid(long sellerid) {
        this.sellerid = sellerid;
    }

    public long getDommodityid() {
        return dommodityid;
    }

    public void setDommodityid(long dommodityid) {
        this.dommodityid = dommodityid;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void confirm(){
        endtime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        status ="F";
    }

    public void cancel(){
        endtime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        status ="C";
    }

    public void undocancel(){
        endtime = null;
        status ="P";
    }



}
