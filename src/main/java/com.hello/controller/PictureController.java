package com.hello.controller;

import com.hello.model.DommodityTpye;
import com.hello.model.Picture;
import com.hello.model.Status;
import com.hello.service.IDommodityService;
import com.hello.service.IPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;


@Controller
@RequestMapping("/picture")
public class PictureController {
    @Resource
    @Autowired
    private IPictureService pictureService;

    private static String picPath = "/var/lib/tomcat8/webapps/helloSSM/pic/";
    //private static String picPath = "F:/text/helloSSM/target/helloSSM/pic/";


    @RequestMapping("uploadPicture")//没有用户检测
    @ResponseBody
    public String uploadPicture(@RequestParam long objectid, @RequestParam MultipartFile pictureFile,@RequestParam String name) throws IOException {
        if(pictureFile==null) return"{\"returncode\":\"201\"}";
        String result = pictureService.insertPic(pictureFile,objectid,name);
        if (result.equals("-1"))return  "{\"returncode\":\"201\"}";
        else return "{\"returncode\":\"200\",\"filename\":\""+result+"\"}";
    }

    @RequestMapping("delete")//没有用户检测
    @ResponseBody
    public String deletePicture(@RequestParam String filename) {
        Picture pic = pictureService.findPicByName(filename);
        long id = pic.getPictureid();
        int result = pictureService.deletePic(id);
        if (result==0)return  "{\"returncode\":\"200\"}";
        else return  "{\"returncode\":\"201\"}";
    }

    @RequestMapping("findAll")//没有用户检测
    @ResponseBody
    public String findAll(@RequestParam long objectid) {
        String[] names = pictureService.findAll(objectid);
        String result = "[";
        String dou = "";
        for(int i=0;i<names.length;i++){
            result+=dou+"\""+names[i]+"\"";
            dou = ",";
        }
        result+="]";
        return  "{\"returncode\":\"200\",\"picnumber\":\""+String.valueOf(names.length)+"\"pictureIdSet\":"+result+"}";

    }

/*
    @RequestMapping("uploadPicture")
    @ResponseBody
    public String uploadPicture(@RequestParam long objectid, @RequestParam MultipartFile pictureFile) throws IOException {
        if(pictureFile==null) return"{\"returncode\":\"201\"}";
        byte[] bs = pictureFile.getBytes();
        int  result = pictureService.insertPic(bs,objectid);
        if (result ==0)return  "{\"returncode\":\"200\"}";
        else return "{\"returncode\":\"201\"}";
    }

    @RequestMapping("downloadPicture")
    @ResponseBody
    public String downloadPicture(@RequestParam long picid){
        Picture pic = pictureService.findPic(picid);
        String bytes = new String(pic.getOrigPic());
        if(pic ==null) return "{\"returncode\":\"201\"}";
        String result = "{\"returncode\":\"200\",\"name\":" + pic.getName()+"\",\"data\":"+bytes +"}";
        return result;
    }
*/
    /**
     * 文件下载
     * @throws IOException
     */
    @RequestMapping(value="/download",method=RequestMethod.GET)
    public void download(@RequestParam(value="filename")String filename,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        //模拟文件，myfile.txt为需要下载的文件
        //String path = request.getSession().getServletContext().getRealPath("/")+"/pic/"+filename;
        String path = picPath+filename;
        System.out.println(path);
        //获取输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));
        //转码，免得文件名中文乱码
        filename = URLEncoder.encode(filename,"UTF-8");
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while((len = bis.read()) != -1){
            out.write(len);
            out.flush();
        }
        out.close();
    }


}

