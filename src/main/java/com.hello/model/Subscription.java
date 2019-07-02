package com.hello.model;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import java.util.jar.JarEntry;

/**
 * author Pei Jiyuan
 * date 2019/4/27
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", String.valueOf(id));
        jsonObject.put("userid", String.valueOf(userid));
        jsonObject.put("dommodityid", String.valueOf(dommodityid));
        return jsonObject.toString();
    }
}
