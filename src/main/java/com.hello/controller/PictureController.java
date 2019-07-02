package com.hello.controller;

import com.hello.model.DommodityTpye;
import com.hello.model.Picture;
import com.hello.model.ReturnCode;
import com.hello.model.Status;
import com.hello.service.IDommodityService;
import com.hello.service.IPictureService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import java.util.HashMap;
import java.util.Map;

/**
 * author Pei Jiyuan
 * datetime 2019/4/27
 * desc
 */

@Controller
@RequestMapping("/picture")
public class PictureController {
    @Resource
    @Autowired
    private IPictureService pictureService;

    // lch server picture storage path
    //private static String picPath = "/var/lib/tomcat8/pic/";

    //local test picture storage path
    private static String picPath = "F:/text/helloSSM/target/helloSSM/pic/";

    public Map<String,String> ReturnCodeMap = ReturnCode.getReturnCodeMap();

    //private Map<String,String> ReturnCodeMap = new UserController().ReturnCodeMap;

    @RequestMapping("uploadPicture")//没有用户检测
    @ResponseBody
    public String uploadPicture(@RequestParam long objectid, @RequestParam MultipartFile pictureFile,@RequestParam String name) {
        JSONObject jsonObject = new JSONObject();
        if(pictureFile.isEmpty()) {
            jsonObject.put("returncode",ReturnCodeMap.get("CantUploadEmptyPicture"));
            return jsonObject.toString();
        }
        String result = pictureService.insertPic(pictureFile, objectid, name);
        if (result.equals("-1")){
            jsonObject.put("returncode",ReturnCodeMap.get("UnexistedDommodity"));
            return jsonObject.toString();
        }
        else {
            jsonObject.put("returncode",ReturnCodeMap.get("Success"));
            jsonObject.put("filename",result);
            return jsonObject.toString();
        }
    }

    @RequestMapping("delete")//没有用户检测
    @ResponseBody
    public String deletePicture(@RequestParam String filename) {
        Picture pic = pictureService.findPicByName(filename);
        long id = pic.getPictureid();
        int result = pictureService.deletePic(id);
        JSONObject jsonObject = new JSONObject();
        switch (result){
            case 0:
                jsonObject.put("returncode",ReturnCodeMap.get("Success"));
                break;
            case -1:
                jsonObject.put("returncode",ReturnCodeMap.get("UnexistedPicture"));
                break;
            case -2:
                jsonObject.put("returncode",ReturnCodeMap.get("LostPicture"));
                break;
            default:
                jsonObject.put("returncode",ReturnCodeMap.get("UnknownReason"));
        }
        return jsonObject.toString();
    }

    @RequestMapping("findAll")//没有用户检测
    @ResponseBody
    public String findAll(@RequestParam long objectid) {
        String[] names = pictureService.findAll(objectid);
        JSONArray picJSONArr = new JSONArray();
        for(int i = 0; i < names.length; i ++){
            picJSONArr.add(names[i]);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("returncode",ReturnCodeMap.get("Success"));
        jsonObject.put("picnumber",String.valueOf(names.length));
        jsonObject.put("pictureIdSet",picJSONArr);
        return jsonObject.toString();

    }

    /**
     * picture download
     * @throws IOException
     */
    @RequestMapping(value="/download",method=RequestMethod.GET)
    public void download(@RequestParam(value="filename")String filename,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        //模拟文件，myfile.txt为需要下载的文件
        String path = picPath + filename;
        try {
            //获取输入流
            InputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));
            //转码，免得文件名中文乱码
            filename = URLEncoder.encode(filename, "UTF-8");
            //设置文件下载头
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            int len = 0;
            while ((len = bis.read()) != -1) {
                out.write(len);
                out.flush();
            }
            out.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}

