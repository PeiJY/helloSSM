package com.hello.model;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Date;

/**
 * author Pei Jiyuan
 * date 2019/4/27
 * desc
 */


public class Dommodity {
    private long id;
    private String name;
    private String description;
    private long ownerid;
    private Status status;
    private Date putawayTime;
    private Date availableTime;
    private long price;
    private String address;
    private String paytype;
    private String[] type;
    private String operation = "S";

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }


    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Dommodity(long id, String name, String description, long ownerid, Status status,String paytype, Date putawayTime, Date availableTime,String operation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerid = ownerid;
        this.status = status;
        this.paytype = paytype;

        this.putawayTime = putawayTime;
        this.availableTime = availableTime;
        this.operation = operation;
    }

    public Dommodity(){
    }

    public long getId() {
        return id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getOwner() {
        return ownerid;
    }

    public void setOwner(long ownerid) {
        this.ownerid = ownerid;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /*public DommodityTpye getType() {
        return type;
    }

    public void setType(DommodityTpye type) {
        this.type = type;
    }*/

    public Date getPutawayTime() {
        return putawayTime;
    }

    public void setPutawayTime(Date putawayTime) {
        this.putawayTime = putawayTime;
    }

    public Date getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(Date availableTime) {
        this.availableTime = availableTime;
    }


    public String toString(String owner) {

        JSONArray typeJSONArr = new JSONArray();
        for(String i: this.type){
            typeJSONArr.add(i);
        }
        JSONObject dommodity = new JSONObject();
        dommodity.put("id",String.valueOf(id));
        dommodity.put("name",name);
        dommodity.put("description",description);
        dommodity.put("owner",owner);
        dommodity.put("status",status);
        dommodity.put("putawayTime",putawayTime);
        dommodity.put("availableTime",availableTime);
        dommodity.put("operation",operation);
        dommodity.put("price",String.valueOf(price));
        dommodity.put("address",address);
        dommodity.put("paytype",paytype);
        dommodity.put("type",typeJSONArr);
        return dommodity.toString();
    }
}
