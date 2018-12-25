package com.hello.controller;

import com.hello.model.User;
import com.hello.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;



@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    @Autowired
    private IUserService userService;

    @RequestMapping ("login")
    @ResponseBody
    public String login(@RequestParam String username,
                      @RequestParam String password, @RequestParam String email){
        User user = this.userService.checkLogin(username,password,email);
        if(user.getId()>0){
            System.out.println("Correct, login successfully");
            return "{\"returncode\":\"200\",\"userinfo\":" + user.returnMassage()+"}";
        }
        else if(user.getId() ==-1){
            System.out.println("User email is unlinked");
            return "{\"returncode\":\"201\"}";
        }
        else{
            System.out.println(username);
            System.out.println(password);
            System.out.println("Username or password wrong");
            return "{\"returncode\":\"201\"}";
        }
    }

    @RequestMapping("logout")
    @ResponseBody
    public String register(@RequestParam long temporaryid){
        System.out.println("logout ");
        long result = this.userService.logout(temporaryid);
        if(result == 0)return "{\"returncode\":\"200\"}";
        else return "{\"returncode\":\"201\"}";
    }

    @RequestMapping("register")
    @ResponseBody
    public String register(@RequestParam String username,
                         @RequestParam String password,
                           @RequestParam String email) {
        System.out.println("Successfully register");
        String result = "";
        long code = this.userService.insertUser(username, password, email);
        if (code > 0) {
            result +="database update successful, no duplicate username and email.";
            result += this.userService.sendEmail(code, username, password, email);
            result += "Emial sending finished, register finished.";
            return "{\"returncode\":\"200\",\"log\":\""+result+"\"}";
        }
        if (code == -1) return "{\"returncode\":\"201\",\"report\":\"username duplicate\"}";//用户名已存在
        if (code == -2) return "{\"returncode\":\"201\",\"report\":\"email address duplicate\"}";//邮箱已被其他账号绑定
        else return "{\"returncode\":\"201\"\"report\":\"unknow resaon\"}";//未知原因
    }

    @RequestMapping("emailLink")
    @ResponseBody
    public String emaillink(@RequestParam String username,@RequestParam long code){
        System.out.println("Successfully emaillink");
        int check = this.userService.emailLink(code,username);
        if(check==0) {
            System.out.println("link successful , code is rigth");
            return "{\"returncode\":\"200\"}";
        }
        if(check==1) {
            System.out.println("link fail , username doesnot exist");
            return "{\"returncode\":\"201\"}";
        }
        if(check==2) {
            System.out.println("link fail , code wrong");
            return "{\"returncode\":\"201\"}";
        }
        else {
            System.out.println("link fail , email address is aready linked by others");
            return "{\"returncode\":\"201\"}";
        }
    }

    @RequestMapping("resendEmail")
    @ResponseBody
    public String resendEmail(@RequestParam String username,@RequestParam String password){
        int check = this.userService.resendEmail(username,password);
        if(check==0) {
            System.out.println("resend successfully");
            return "{\"returncode\":\"200\"}";
        }
        else {
            System.out.println("resend fail");
            return "{\"returncode\":\"201\"}";
        }
    }

    @RequestMapping("changeEmail")
    @ResponseBody
    public String changeEmail(@RequestParam String username,@RequestParam String password,@RequestParam String email){
        int check = this.userService.changeEmail(username,password,email);
        if(check==0) {
            System.out.println("change successfully");
            return "{\"returncode\":\"200\"}";
        }
        else {
            System.out.println("change fail");
            return "{\"returncode\":\"201\"}";
        }
    }

    @RequestMapping("json")
    @ResponseBody
    public String jsonTest(@RequestBody String requestuser){
        System.out.println("json controll call successful");
        System.out.println(requestuser);
        System.out.println(requestuser.substring(4,15));
        return requestuser;
    }

}
