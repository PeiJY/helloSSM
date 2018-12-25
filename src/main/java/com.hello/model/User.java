package com.hello.model;

import java.util.Date;

/**
 * project helloSSM
 * authod wuyanhui
 * datetime 2017/11/23 16:17
 * desc
 */

public class User {
    private long id;
    private String email;
    private String password;
    private String username;
    private long code;
    private long linked;
    private long temporaryid;

    public long getTemporaryid(){return temporaryid;}

    public void setTemporaryid(long temporaryid){this.temporaryid=temporaryid;}

    public User() {
        System.out.println("model successful");
        this.id = 1;
        this.password = "1";
        this.username= "1";
        this.temporaryid = 0;
    }

    public User(long id,String username, String password){
        this.id = id;
        this.password = password;
        this.username = username;
        this.temporaryid = 0;
    }

    public User(long id,String username, String password, String email){
        this.id = id;
        this.password = password;
        this.username = username;
        this.email = email;
        this.temporaryid = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail(){return email;}

    public void setEmail(String e){email=e;}

    public long getLinked(){return linked;}

    public void setLinked(long l){linked=l;}

    public void setCode(long code){this.code=code;}

    public long getCode(){return this.code;}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", code='" + String.valueOf(code) + '\'' +
                ",temporaryid= '"+ String.valueOf(temporaryid)+"\'"+
                '}';
    }

    public String returnMassage(){
        return "{"+
                "\"username\":\"" + username + '\"' +
                ",\"email\":\"" + email + '\"' +
                ",\"temporaryid\":\""+ String.valueOf(temporaryid)+"\""+"}";
    }
}
