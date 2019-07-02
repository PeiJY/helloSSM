package com.hello.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;
import com.hello.dao.IUserDao;
import com.hello.model.User;
import com.hello.service.IUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author Pei Jiyuan
 * date 2019/4/27
 * desc
 */

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public long insertUser(String username, String password,String email){
        User user = new User(10,username,password,email);

        /** verify the uniqueness */

        int others1 = userDao.findByEmail(user.getEmail()).length;
        if(others1>0) return -2;// newEmail is bound by other user
        int others2 = userDao.findByUsername(user.getUsername()).length;
        if(others2>0)return -1; // username is bound by other user

        /** generate a unique verification code for newEmail address binding */
        long code = 1;
        Random rand =new Random();
        code=rand.nextInt(100000)+1;
        User u = userDao.findByCode(code);
        while(u != null){
            code = rand.nextInt(100000)+1;
            u = userDao.findByCode(code);
        }
        user.setCode(code);

        /** encrypt the password in database */
        String passwordHash = passwordToHash(user.getPassword());
        user.setPassword(passwordHash);

        userDao.insertUser(user);
        return code;
    }

    @Override
    public long logout(long temporaryid){
        User user = userDao.findByTemporaryID(temporaryid);
        if(user == null){
            return -1;//没有对应temporaryid的用户，可能未登录
        }
        user.setTemporaryid(0);
        userDao.modifyUser(user);
        return 0;
    }

    @Override
    public User checkLogin(String username, String password, String email) {
        /** build the login user */
        User userLogin = new User();
        userLogin.setPassword(password);
        userLogin.setUsername(username);
        userLogin.setEmail(email);

        /** search the corresponding user in database */
        User userInDataBase = new User();
        long temporaryid;
        Random rand = new Random();
        temporaryid = rand.nextInt(100000)+1;
        if(!username.equals("-1") && email.equals("-1")){ // use username to search
            userInDataBase = findLinkedUserByUsername(username);
        }else if(username.equals("-1") && !email.equals("-1")){ // use newEmail to search
            userInDataBase = findLinkedUserByEmail(email);
        }
        else{ // searching method is incorrect
            System.out.println("username is "+username);
            System.out.println("newEmail is "+email);
            userInDataBase.setId(-3);
            return userInDataBase;
        }

        /** compare the password */
        if(userInDataBase != null && comparePassword(userLogin,userInDataBase)){ // password is correct
            if(userInDataBase.getLinked() == 1){ // newEmail address linked
                User u = userDao.findByTemporaryID(temporaryid);
                while(u != null){
                    temporaryid=rand.nextInt(100000)+1;
                    u = userDao.findByCode(temporaryid);
                }
                userInDataBase.setTemporaryid(temporaryid);
                System.out.println(userInDataBase);
                userDao.modifyUser(userInDataBase);
                return userInDataBase;
            }
            else { // newEmail address unlinked
                userInDataBase.setId(-1);
                return userInDataBase;
            }
        }
        else { // password is incorrect
            userInDataBase = new User();
            userInDataBase.setId(-2);
            return userInDataBase;
        }
    }

    @Override
    public int emailLink(long code, String username){

        User user = userDao.findByCode(code);
        if(user == null) return -1;// code is incorrect

        if(user.getUsername().equals(username)) {
            user.setLinked(1);
            int others1 = userDao.findByEmail(user.getEmail()).length;
            if(others1>0) return -3;// newEmail is bound by other user
            int others2 = userDao.findByUsername(user.getUsername()).length;
            if(others2>0)return -4; // username is bound by other user
            userDao.modifyUser(user);
            return 0;
        }
        else return -2;// username is incorrect
    }

    @Override
    public User findLinkedUserByUsername(String username){
        /** in case of multiple unlinked user existed with same username */
        User[] usersInDataBase = userDao.findByUsername(username);
        for(User user : usersInDataBase ){
            if( user.getLinked() == 1){
                return user;
            }
        }
        return null;
    }

    @Override
    public User findLinkedUserByEmail(String email){
        /** in case of multiple unlinked user existed with same newEmail */
        User[] usersInDataBase = userDao.findByEmail(email);
        for(User user : usersInDataBase ){
            if( user.getLinked() == 1){
                return user;
            }
        }
        return null;
    }

    public int resendEmail(String username,String password){
        User userInDataBase = findLinkedUserByUsername(username);
        if(userInDataBase != null && userInDataBase.getPassword().equals(password)){
            String check = sendEmail(userInDataBase.getCode(),username,password,userInDataBase.getEmail());
            if(check.equals("newEmail setting finished.newEmail sending finished."))
                return 0;
            else
                return -2;// newEmail sending fail
        }else
            return -1; // username and password is unmatched or not existed
    }

    public int changeEmail(String username, String password, String newEmail){

        User userInDataBase = findLinkedUserByUsername(username);
        if(userInDataBase != null && userInDataBase.getPassword().equals(password)){
            String check = sendEmail(userInDataBase.getCode(),username,password, newEmail);
            if(check.equals("newEmail setting finished.newEmail sending finished.")){
                userInDataBase.setEmail(newEmail);
                userInDataBase.setLinked(0);
                userDao.modifyUser(userInDataBase);
                return 0;
            }
        }
        return -1; //用户名密码错误
    }
    public int changePassword(String username, String password, String newpassword) {
        User user = findLinkedUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            user.setPassword(newpassword);
            userDao.modifyUser(user);
            return 0;
        }
        return -1; //用户名密码错误
    }


    @Override
    public String sendEmail(long code, String username, String password, String email){
        System.out.println("sendemail call successful");
        MailService s = new MailService();
        String emessage = "This is a Email for SUSTechFM account link.<br>This is your check code:" + String.valueOf(code) + "<br>If you have not registered a account in SUSTechFm, please ignore this newEmail.";
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
    public User[] findUsersByUsername(String username){
        User user[] = userDao.findByUsername(username);
        return user;
    }

    @Override
    public boolean comparePassword(User userLogin, User userInDataBase) {
        return Objects.equals(passwordToHash(userLogin.getPassword()), userInDataBase.getPassword()); // 数据库中的 password 已经是 hash，不用转换
    }
/*
    @Override
    public Object temporaryidVerfy(long temporaryid){
        JSONObject jsonObject = new JSONObject();
        if(temporaryid == 0){
            jsonObject.put("returncode","3005");
            return jsonObject;
        }
        User user = findUserByTemporaryid(temporaryid);
        if(user == null){
            jsonObject.put("returncode","3005");
            return jsonObject;
        }
        return user;
    }
*/
    private String passwordToHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes());
            byte[] src = digest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            // 字节数组转16进制字符串
            for (byte aSrc : src) {
                String s = Integer.toHexString(aSrc & 0xFF);
                if (s.length() < 2) {
                    stringBuilder.append('0');
                }
                stringBuilder.append(s);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;
    }

}
