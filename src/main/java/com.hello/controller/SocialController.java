package com.hello.controller;

import com.hello.model.ChatRecord;
import com.hello.model.Subscribe;
import com.hello.model.User;
import com.hello.service.ISocialService;
import com.hello.service.IUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;


@Controller
@RequestMapping("/social")
public class SocialController {
    @Resource
    @Autowired
    private IUserService userService;
    @Resource
    @Autowired
    private ISocialService socialService;

    @RequestMapping ("charRecord")//用于接收数据库中发给自己的的聊天记录
    @ResponseBody
    public String login(@RequestParam long temporaryid) {
        User user = userService.findUserByTemporaryid(temporaryid);
        if(user==null)return "{\"returncode\":\"201\"}";
        ChatRecord records[] = socialService.getChatRecord(user);
        //JSONObject jsonObj = JSONObject.fromObject("{\"returncode\":\"201\"}");
        String result = "{\"returncode\":\"200\",\"records\":";
        String rs = "[";
        String dou = "";
        for(ChatRecord i:records){
            rs += dou + i.returnMassage();
            dou = ",";
        }
        rs += "]";
        result+=rs+"}";
        return  result;
        //jsonObj.put("records",rs);
        //return jsonObj.toString();
    }

    @RequestMapping ("subscribe")
    @ResponseBody
    public String subscribe(@RequestParam long temporaryid,@RequestParam String name) {
        User user = userService.findUserByTemporaryid(temporaryid);
        if(user==null)return "{\"returncode\":\"201\"}";
        int code = socialService.subscribe(user,name);
        if(code >=0)return  "{\"returncode\":\"200\"}";
        else return "{\"returncode\":\"201\"}";
    }

    @RequestMapping ("unSubscribe")
    @ResponseBody
    public String unSubscribe(@RequestParam long temporaryid,@RequestParam String name) {
        User user = userService.findUserByTemporaryid(temporaryid);
        if(user==null)return "{\"returncode\":\"201\"}";
        int code = socialService.unSubscribe(user,name);
        if(code >=0)return  "{\"returncode\":\"200\"}";
        else return "{\"returncode\":\"201\"}";
    }

    @RequestMapping ("subscribeList")
    @ResponseBody
    public String subscribeList(@RequestParam long temporaryid) {
        User user = userService.findUserByTemporaryid(temporaryid);
        if(user==null)return "{\"returncode\":\"201\"}";
        Subscribe subscribeList[] = socialService.subscribeList(user);
        //JSONObject jsonObj = JSONObject.fromObject("{\"returncode\":\"200\"}");
        String result = "{\"returncode\":\"200\",\"subscribeList\":";
        String rs = "[";
        String dou = "";
        for(Subscribe i:subscribeList){
            rs += dou + i.returnMassage();
            dou = ",";
        }
        rs += "]";
        result+=rs+"}";
        //jsonObj.put("subscribeList",rs);
        //return jsonObj.toString();
        return result;
    }

}
