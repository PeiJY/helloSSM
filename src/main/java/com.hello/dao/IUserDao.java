package com.hello.dao;

import com.hello.model.User;

/**
 * project helloSSM
 * authod wuyanhui
 * datetime 2017/11/23 16:17
 * desc
 */
public interface IUserDao {
    User selectUser(long id);
    User findByTemporaryID(long temporaryid);
    void insertUser(User user);
    User findByUsername(String username);
    User findByCode(long code);
    void modifyUser(User user);
    User findByEmail(String eamil);
    long findLinkedNumByEmail(String email);
    long findLinkedNumByUsername(String username);
}
