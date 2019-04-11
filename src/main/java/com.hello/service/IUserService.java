package com.hello.service;

import com.hello.model.User;

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

    public User findUserByUsername(String username);

}
