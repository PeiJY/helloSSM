package com.hello.controller;

import com.hello.model.ReturnCode;
import com.hello.model.User;
import com.hello.service.IUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;


/**
 * author Pei Jiyuan
 * datetime 2019/6/28
 */


@Controller
@RequestMapping("/user")
public class UserController {

    public Map<String,String> ReturnCodeMap = ReturnCode.getReturnCodeMap();

    @Resource
    @Autowired
    private IUserService userService;

    @RequestMapping ("login")
    @ResponseBody
    public String login(@RequestParam String username,
                      @RequestParam String password, @RequestParam String email){
        // TODO handle the cast that multi user have same username, but just one is linked
        User user = this.userService.checkLogin(username,password,email);
        JSONObject jsonObject = new JSONObject();
        switch ((int)user.getId()){
            case -1:
                jsonObject.put("returncode",ReturnCodeMap.get("UnlinkedAccount"));
                break;
            case -2:
                jsonObject.put("returncode",ReturnCodeMap.get("IncorrectUserInfo"));
                break;
            case -3:
                jsonObject.put("returncode",ReturnCodeMap.get("IncorrectLoginInfo"));
                break;
            default:
                jsonObject.put("returncode",ReturnCodeMap.get("Success"));
                jsonObject.put("userinfo",user.toString());
        }
        return jsonObject.toString();
    }

    @RequestMapping("logout")
    @ResponseBody
    public String register(@RequestParam long temporaryid){
        JSONObject jsonObject = new JSONObject();
        int result = (int)this.userService.logout(temporaryid);
        switch (result){
            case 0:
                jsonObject.put("returncode",ReturnCodeMap.get("Success"));
                break;
            case -1:
                jsonObject.put("returncode",ReturnCodeMap.get("UnloginUser"));
                break;
            default:
                jsonObject.put("returncode",ReturnCodeMap.get("UnknownReason"));
        }
        return jsonObject.toString();
    }

    @RequestMapping("register")
    @ResponseBody
    public String register(@RequestParam String username,
                         @RequestParam String password,
                           @RequestParam String email) {
        JSONObject jsonObject = new JSONObject();
        long code = this.userService.insertUser(username, password, email);

        switch ((int)code){
            case -1:
                jsonObject.put("returncode",ReturnCodeMap.get("DuplicatedUsername"));
                break;
            case -2:
                jsonObject.put("returncode",ReturnCodeMap.get("DuplicatedEmail"));
                break;
            default:
                String result = "";
                result +="Database update successful, no duplicate username and email.";
                result += this.userService.sendEmail(code, username, password, email);
                result += "Emial sending finished, register finished.";
                jsonObject.put("returncode",ReturnCodeMap.get("Success"));
                //jsonObject.put("log",result);
                System.out.println("Register email sending log : "+ result);
                return jsonObject.toString();
        }
        return jsonObject.toString();
    }

    @RequestMapping("emailLink")
    @ResponseBody
    public String emaillink(@RequestParam String username, @RequestParam long code){
        JSONObject jsonObject = new JSONObject();
        int check = this.userService.emailLink(code, username);

        switch (check){
            case 0:
                jsonObject.put("returncode",ReturnCodeMap.get("Success"));
                break;
            case -1:
                jsonObject.put("returncode",ReturnCodeMap.get("IncorrectBindingCode"));
                break;
            case -2:
                jsonObject.put("returncode",ReturnCodeMap.get("UnexistedUsername"));
                break;
            case -3:
                jsonObject.put("returncode",ReturnCodeMap.get("DuplicatedEmail"));
                break;
            case -4:
                jsonObject.put("returncode",ReturnCodeMap.get("DuplicatedUsername"));
                break;
            default:
                jsonObject.put("returncode",ReturnCodeMap.get("UnknownReason"));
        }
        return jsonObject.toString();
    }

    @RequestMapping("resendEmail")
    @ResponseBody
    public String resendEmail(@RequestParam String username, @RequestParam String password){
        JSONObject jsonObject = new JSONObject();
        int check = this.userService.resendEmail(username,password);
        switch (check) {
            case 0:
                jsonObject.put("returncode", ReturnCodeMap.get("Success"));
                break;
            case -1:
                jsonObject.put("returncode", ReturnCodeMap.get("IncorrectUserInfo"));
                break;
            case -2:
                jsonObject.put("returncode", ReturnCodeMap.get("UnknownReason"));
                break;
        }
        return jsonObject.toString();
    }

    @RequestMapping("changeEmail")
    @ResponseBody
    public String changeEmail(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String email){
        JSONObject jsonObject = new JSONObject();
        int check = this.userService.changeEmail(username,password,email);
        switch (check) {
            case 0:
                jsonObject.put("returncode", ReturnCodeMap.get("Success"));
                break;
            case -1:
                jsonObject.put("returncode", ReturnCodeMap.get("IncorrectUserInfo"));
                break;
        }
        return jsonObject.toString();
    }

    @RequestMapping("changePassword")
    @ResponseBody
    public String changePassword(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String newpassword){
        int check = this.userService.changePassword(username,password,newpassword);
        JSONObject jsonObject = new JSONObject();
        switch (check) {
            case 0:
                jsonObject.put("returncode", ReturnCodeMap.get("Success"));
                break;
            case -1:
                jsonObject.put("returncode", ReturnCodeMap.get("IncorrectUserInfo"));
                break;
        }
        return jsonObject.toString();
    }
}
