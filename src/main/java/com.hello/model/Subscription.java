package com.hello.model;

/**
 * project helloSSM
 * authod wuyanhui
 * datetime 2017/11/23 16:17
 * desc
 */

public class Subscription {
    private long id;
    private long userid;
    private long dommodityid;
    private String time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getDommodityid() {
        return dommodityid;
    }

    public void setDommodityid(long dommodityid) {
        this.dommodityid = dommodityid;
    }

    public Subscription(long userid, long dommodityid,String time) {
        System.out.println("model successful");
        this.id = 1;
        this.userid = userid;
        this.dommodityid= dommodityid;
        this.time  = time;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userid='" + String.valueOf(userid) + '\'' +
                ", dommodityid='" + String.valueOf(dommodityid) + '\'' +
                '}';
    }
}
