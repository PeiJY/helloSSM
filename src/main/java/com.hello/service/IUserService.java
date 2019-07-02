package com.hello.service;

import com.hello.model.User;
/**
 * author Pei Jiyuan
 * date 2019/4/27
 * desc
 */

public interface IUserService {

   // public User selectUser(long userId);

    public long insertUser(String username, String password,String email);

    public User checkLogin(String username,String password,String email);

    public String sendEmail(long code,String username, String password, String email);

    public int emailLink(long code,String username);

    public int resendEmail(String username,String password);

    public long logout(long temporaryid);

    public int changeEmail(String username, String password,String email);

    public int changePassword(String username, String password,String newpassword);

    public User findUser(long userid);

    public User findUserByTemporaryid(long temporaryid);

    public User[] findUsersByUsername(String username);//!!!!

    public boolean comparePassword(User loginUser, User userInDataBase);

    public User findLinkedUserByUsername(String username);

    public User findLinkedUserByEmail(String email);

   // public Object temporaryidVerfy(long temporaryid);


}
