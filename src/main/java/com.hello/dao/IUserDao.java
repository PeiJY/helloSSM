package com.hello.dao;

import com.hello.model.User;

/**
 * authod Pei Jiyuan
 * datetime 2019/4/27
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
