package com.hello.dao;

import com.hello.model.Picture;
import com.hello.model.User;

import java.io.File;

/**
 * author Pei Jiyuan
 * date 2019/4/27
 * desc
 */

public interface IPictureDao {
    Picture selectPicture(long pictureid);
    void insertPicture(Picture picture);
    //String findName(long picid);
    void modifyPic(Picture pic);
    void deletePic(long picid);
    Picture[] findAllByObjectId(long objectid);
    Picture findByName (String finename);
}
