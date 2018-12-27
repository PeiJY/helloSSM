package com.hello.model;

import java.util.Date;

/**
 * project helloSSM
 * authod wuyanhui
 * datetime 2017/11/23 16:17
 * desc
 */

public class Subscribe {
    private long id;
    private long scruserid;
    private String tousername;

    public Subscribe(long scruserid, String tousername) {
        this.scruserid = scruserid;
        this.tousername = tousername;
    }
    public Subscribe(){}
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getScruserid() {
        return scruserid;
    }

    public void setScruserid(long scruserid) {
        this.scruserid = scruserid;
    }

    public String getTousername() {
        return tousername;
    }

    public void setTousername(String tousername) {
        this.tousername = tousername;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" +String.valueOf(id) +
                ", scruserid='" + String.valueOf(scruserid) + '\'' +
                ", tousername='" + tousername + '\'' +
                '}';
    }

    public String returnMassage(){
        return "\""+tousername+"\"";
    }
}
