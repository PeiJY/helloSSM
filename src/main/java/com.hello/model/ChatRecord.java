package com.hello.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * authod Pei Jiyuan
 * datetime 2019/4/27
 * desc
 */

public class ChatRecord {
    private long id;
    private String message;
    private Date time;
    private String srcname;
    private String recvname;

    public String getSrcname() {
        return srcname;
    }

    public void setSrcname(String srcname) {
        this.srcname = srcname;
    }

    public String getRecvname() {
        return recvname;
    }

    public void setRecvname(String recvname) {
        this.recvname = recvname;
    }

    public ChatRecord(String message) {
        String contents[] = message.split(",");
        this.srcname = contents[0];
        this.recvname = contents[2];
        this.message = message.substring(contents[0].length()+contents[1].length()+contents[2].length()+3);
        this.time = new Date();
    }
    public ChatRecord(){}
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" +String.valueOf(id) +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String returnMassage(){
        return "{"+
                "\"id\":\"" + String.valueOf(id) + '\"' +
                ",\"srcname\":\"" + srcname + '\"' +
                ",\"recvname\":\"" + recvname + '\"' +
                ",\"message\":\"" + message + '\"' +
                ",\"time\":\""+ time+"\""+"}";
    }
}
