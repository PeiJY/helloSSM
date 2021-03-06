package com.hello.model;

import net.sf.json.JSONObject;

/**
 * author Pei Jiyuan
 * date 2019/4/27
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

    //TODO what code is for

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


    public String toString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",username);
        jsonObject.put("email",email);
        jsonObject.put("temporaryid",String.valueOf(temporaryid));
        return jsonObject.toString();
    }
}
