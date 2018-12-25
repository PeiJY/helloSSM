package com.hello.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * project helloSSM
 * authod wuyanhui
 * datetime 2017/11/23 16:17
 * desc
 */

public class Picture {
    private long pictureid;
    private long objectid;
    private String name;
    private MultipartFile pic;
    private String filename;

//private byte[] origPic;


    public String getName() {
        return name;
    }



    public Picture(MultipartFile pic,long objectid ,String name){//,byte[] origPic) {
        this.objectid = objectid;
        //this.origPic = origPic;
        this.pictureid  = 1;
        this.pic = pic;
        this.name = name;
    }

    public Picture(long objectid ,String name){
        this.objectid = objectid;
        this.name = name;
    }

    public Picture(){};



    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getPictureid() {
        return pictureid;
    }

    public void setPictureid(long pictureid) {
        this.pictureid = pictureid;
    }

    public long getObjectid() {
        return objectid;
    }

    public void setObjectid(long objectid) {
        this.objectid = objectid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getPic() {
        return pic;
    }

    public void setPic(MultipartFile pic) {
        this.pic = pic;
    }
/*
    public byte[] getOrigPic() {
        return origPic;
    }

    public void setOrigPic(byte[] origPic) {
        this.origPic = origPic;
    }*/
}
