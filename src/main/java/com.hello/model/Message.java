package com.hello.model;
import net.sf.json.JSONObject;

import javax.json.JsonObject;
import java.sql.Timestamp;
import java.util.Date;

/**
 * author Pei Jiyuan
 * date 2019/4/27
 * desc
 */

public class Message {

    private int messageId;
    private int fromId;
    private String fromName;
    private int toId;
    private String messageText;
    private Timestamp messageDate;

    public Message() {
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Timestamp getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Timestamp messageDate) {
        this.messageDate = messageDate;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messageId",String.valueOf(messageId));
        jsonObject.put("fromId",String.valueOf(fromId));
        jsonObject.put("fromName",fromName);
        jsonObject.put("toId",String.valueOf(toId));
        jsonObject.put("messageText",messageText);
        jsonObject.put("messageDate",messageDate);
        return jsonObject.toString();
    }
}
