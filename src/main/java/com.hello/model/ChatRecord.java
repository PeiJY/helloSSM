package com.hello.model;

import net.sf.json.JSONObject;

import javax.json.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author Pei Jiyuan
 * date 2019/4/27
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
    public String toString(){
        JSONObject chatRecord = new JSONObject();
        chatRecord.put("id",String.valueOf(id));
        chatRecord.put("srcname",srcname);
        chatRecord.put("recvname",recvname);
        chatRecord.put("message",message);
        chatRecord.put("time",time.toString());
        return chatRecord.toString();
    }
}
