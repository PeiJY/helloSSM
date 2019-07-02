package com.hello.service;

import com.hello.model.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
/**
 * author Pei Jiyuan
 * date 2019/4/27
 * desc
 */

public interface IPictureService {

/*
    public File generateLowD(File picture);
*/
    public String insertPic(MultipartFile picture, long objectid,String name);

    public Picture findPic(long picid);

    public Picture findPicByName(String filename);

    public int deletePic(long pictureid);

    public  String[] findAll(long objectid);
}
