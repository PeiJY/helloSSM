package com.hello.service.impl;
import java.util.Random;
import com.hello.dao.IUserDao;
//import com.hello.dao.impl.UserDaoImpl;
import com.hello.model.User;
import com.hello.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.annotation.Resource;

@Service
public class UserServiceImpl implements IUserService {



    @Autowired
   private IUserDao userDao;



    @Override
    public long insertUser(String username, String password,String email){
        User user = new User(10,username,password,email);
        long check = userDao.findLinkedNumByUsername(username);
        if(check>0)return-1;//用户名已存在
        long num = userDao.findLinkedNumByEmail(email);
        if(num >0 )return-2;//email已被注册
        long code = 1;
        Random rand =new Random();
        code=rand.nextInt(100000)+1;
        User u = userDao.findByCode(code);
        while(u != null){
            code=rand.nextInt(100000)+1;
            u = userDao.findByCode(code);
        }
        user.setCode(code);
        System.out.println(user.toString());
        userDao.insertUser(user);
        System.out.println(code);
        return code;
    }

    @Override
    public long logout(long temporaryid){
        User u = userDao.findByTemporaryID(temporaryid);
        if(u == null){
            return -1;//没有对应temporaryid的用户，可能未登录
        }
        u.setTemporaryid(0);
        userDao.modifyUser(u);
        return 0;
    }

    @Override
    public User checkLogin(String username, String password,String email) {
        User user = new User();
        long temporaryid = 1;
        Random rand =new Random();
        temporaryid=rand.nextInt(100000)+1;
        if(!username.equals("-1") && email.equals("-1")){
            user = userDao.findByUsername(username);
        }else if(username.equals("-1") && !email.equals("-1")){
            user = userDao.findByEmail(email);
        }
        else{
            System.out.println("username is "+username);
            System.out.println("email is "+email);
            user.setId(-3);
            return user;
        }
        if(user != null && user.getPassword().equals(password)){
            if(user.getLinked()==1){
                User u = userDao.findByTemporaryID(temporaryid);
                while(u != null){
                    temporaryid=rand.nextInt(100000)+1;
                    u = userDao.findByCode(temporaryid);
                }
                user.setTemporaryid(temporaryid);
                System.out.println(user);
                userDao.modifyUser(user);
                return user;
            }
            else {
                user.setId(-1);
                return user; //未Link
            }
        }
        user = new User();
        user.setId(-2);
        return user; //用户名密码错误
    }

    @Override
    public int emailLink(long code,String username){
        User user = userDao.findByCode(code);
        if(user == null) return 1;
        System.out.println(user.getUsername()+":"+username);
        if(user.getUsername().equals(username)) {
            //userDao.emailLink(user.getId());
            user.setLinked(1);
            long others = userDao.findLinkedNumByEmail(user.getEmail());
            if(others>0) return 3;//此邮箱已被其他用户绑定
            long others1 = userDao.findLinkedNumByUsername(username);
            if(others1>0)return 4;
            userDao.modifyUser(user);
            return 0;
        }
        else return 2;
    }

    public int resendEmail(String username,String password){
        User user = userDao.findByUsername(username);
        if(user != null && user.getPassword().equals(password)){
            sendEmail(user.getCode(),username,password,user.getEmail());
            return 0;
        }
        return 2; //用户名密码错误
    }

    public int changeEmail(String username, String password,String email){
        User user = userDao.findByUsername(username);
        if(user != null && user.getPassword().equals(password)){
            String check = sendEmail(user.getCode(),username,password,email);
            if(check.equals("email setting finished.email sending finished.")){
                user.setEmail(email);
                user.setLinked(0);
                userDao.modifyUser(user);
                return 0;
            }
        }
        return 2; //用户名密码错误
    }


    @Override
    public String sendEmail(long code, String username, String password, String email){
        System.out.println("sendemail call successful");
        MailService s = new MailService();
        String emessage = "This is a Email for SUSTechFM account link.<br>This is your check code:" + String.valueOf(code) + "<br>If you have not registered a account in SUSTechFm, please ignore this email.";
        try {
            String a =s.sendHtmlMail(email, "SUSTechFM account link", emessage);
            System.out.println("sendemail  successful");
            return a;
        }
        catch (Exception e){
            System.out.println("sendemail fail");
            e.printStackTrace();
            String a = e.getMessage();
            return a;
        }
    }
    @Override
    public User findUser(long userid){
        User user = userDao.selectUser(userid);
        return user;
    }

    @Override
    public User findUserByTemporaryid(long temporaryid){
        User user = userDao.findByTemporaryID(temporaryid);
        return user;
    }

    @Override
    public User findUserByUsername(String username){
        User user = userDao.findByUsername(username);
        return user;
    }
}
