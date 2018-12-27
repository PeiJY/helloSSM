package com.hello.service.impl;

import com.hello.dao.IDommodityDao;
import com.hello.dao.IDommodityTypeDao;
import com.hello.dao.IPictureDao;
import com.hello.dao.IUserDao;
import com.hello.model.*;
import com.hello.service.IPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;



@Service
public class PictureServiceImpl implements IPictureService {


    @Autowired
   private IDommodityDao dommodityDao;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IDommodityTypeDao dommodityTypeDao;
    @Autowired
    private IPictureDao pictureDao;

    private static String picPath = "/var/lib/tomcat8/webapps/helloSSM/pic/";
    //private static String picPath = "F:/text/helloSSM/target/helloSSM/pic/";
    private String sls = "/";
   //private String sls = "\\";

/*
    @Override
    public File generateLowD(File picture) {
    }
*/


    @Override
    public String insertPic(MultipartFile picturefile, long objectid,String name)  {
        Dommodity object = dommodityDao.selectDommodity(objectid);
        if(object==null)return"-1";//商品不存在
        Picture pic = new Picture(objectid,name);
        pictureDao.insertPicture(pic);
        String filename = String.valueOf(pic.getPictureid())+"-"+pic.getName();


        name = picturefile.getOriginalFilename();//直接返回文件的名字
        String subffix = name.substring(name.lastIndexOf(".") + 1, name.length());//我这里取得文件后缀
        File file=new File(picPath);
        if(!file.exists()){//目录不存在就创建
                file.mkdirs();
        }
        filename = filename+'.'+subffix;
        try {
            picturefile.transferTo(new File(file + sls + filename));//保存文件
            pic.setFilename(filename);
            pictureDao.modifyPic(pic);
        }catch (IOException e){
            System.out.println(e.getMessage());
            filename = e.getMessage();
        }
        return filename;
    }

    @Override
    public Picture findPic(long picid) {
        Picture pic = pictureDao.selectPicture(picid);
        return pic;
    }

/*
    @Override
    public int modifyPic(File pitture, long picid) {
    }
*/

    @Override
    public Picture findPicByName(String filename){
        Picture pic = pictureDao.findByName(filename);
        return pic;
    }

    @Override
    public int deletePic(long picid) {
        Picture pic = pictureDao.selectPicture(picid);
        if(pic==null)return-1;
        String filename = pic.getFilename();
        pictureDao.deletePic(picid);
        File file=new File(picPath+"/"+filename);
        if(file.exists()&&file.isFile()) {
            file.delete();
        }
        else return-2;
        return 0;
    }

    @Override
    public  String[] findAll(long objectid){
        Picture[] pics = pictureDao.findAllByObjectId(objectid);
        String[] ids = new String[pics.length];
        for(int i=0;i<pics.length;i++){
            ids[i] = pics[i].getFilename();
        }
        return ids;

    }


}
