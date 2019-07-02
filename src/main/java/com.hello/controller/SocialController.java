package com.hello.controller;

import com.hello.model.ChatRecord;
import com.hello.model.ReturnCode;
import com.hello.model.Subscribe;
import com.hello.model.User;
import com.hello.service.ISocialService;
import com.hello.service.IUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.Map;

/**
 * author Pei Jiyuan
 * datetime 2019/4/27
 * desc
 */

@Controller
@RequestMapping("/social")
public class SocialController {
    @Resource
    @Autowired
    private IUserService userService;
    @Resource
    @Autowired
    private ISocialService socialService;

    //private Map<String,String> ReturnCodeMap = new UserController().ReturnCodeMap;


    public Map<String,String> ReturnCodeMap = ReturnCode.getReturnCodeMap();

    @RequestMapping ("charRecord")//用于接收数据库中发给自己的的聊天记录
    @ResponseBody
    public String login(@RequestParam long temporaryid) {
        JSONObject jsonObject = new JSONObject();

        /** login verify */
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        ChatRecord records[] = socialService.getChatRecord(user);
        JSONArray chatRecordJSONArr = new JSONArray();
        for(ChatRecord i:records){
            chatRecordJSONArr.add(i.toString());
        }
        jsonObject.put("returncode",ReturnCodeMap.get("Success"));
        jsonObject.put("records",chatRecordJSONArr);
        return jsonObject.toString();
    }

    @RequestMapping ("subscribe")
    @ResponseBody
    public String subscribe(@RequestParam long temporaryid,@RequestParam String name) {
        JSONObject jsonObject = new JSONObject();

        /** login verify */
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        int code = socialService.subscribe(user, name);
        if(code == -1) {
            jsonObject.put("returncode",ReturnCodeMap.get("DuplicatedSubscription"));
            return jsonObject.toString();
        }else {
            jsonObject.put("returncode",ReturnCodeMap.get("Success"));
            return jsonObject.toString();
        }
    }

    @RequestMapping ("unSubscribe")
    @ResponseBody
    public String unSubscribe(@RequestParam long temporaryid,@RequestParam String name) {
        JSONObject jsonObject = new JSONObject();

        /** login verify */
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        int code = socialService.unSubscribe(user,name);
        if(code == -1) {
            jsonObject.put("returncode",ReturnCodeMap.get("UnexistedOrder"));
            return jsonObject.toString();
        }else {
            jsonObject.put("returncode",ReturnCodeMap.get("Success"));
            return jsonObject.toString();
        }
    }

    @RequestMapping ("subscriptionList")
    @ResponseBody
    public String subscriptionList(@RequestParam long temporaryid) {
        JSONObject jsonObject = new JSONObject();

        /** login verify */
        User user = userService.findUserByTemporaryid(temporaryid);
        if(temporaryid == 0 || user == null){
            jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
            return jsonObject.toString();
        }

        Subscribe subscribeList[] = socialService.subscribeList(user);
        JSONArray subscriptionJSONArr = new JSONArray();
        for(Subscribe i:subscribeList){
            subscriptionJSONArr.add(i.returnMassage());
        }
        jsonObject.put("returncode",ReturnCodeMap.get("Success"));
        jsonObject.put("subscriptionList",subscriptionJSONArr);
        return jsonObject.toString();
    }
}
