package com.hello.model;

import java.util.Date;

/**
 * authod Pei Jiyuan
 * datetime 2019/4/27
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
        //this.type = type;
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

    @Override
    public String toString() {
        return "Commodity{" +
                "id=" + String.valueOf(id) +
                ", name='" + name + '\'' +'\n'+
                ", owner='" + String.valueOf(ownerid) + '\'' +'\n'+
                ", status='" + status + '\'' +
                ", paytype=" + paytype +
                ", type='" + type + '\'' +
                ", operation='"+operation+'\''+

                ", putawayTime='" + putawayTime + '\'' +
                ", availableTime='" + availableTime + '\'' +
                '}';
    }


    public String Message(String owner) {
        String types = "[";
        String dou = "";
        for(String i: this.type){
            types+=dou+"\""+i+"\"";
            dou = ",";
        }
        types +="]";
        return "{" +
                "\"id\":\"" + id +"\""+
                ", \"name\":\"" + name + "\"" +
                ", \"description\":\"" + description + '\"' +
                ", \"owner\":\"" + owner + '\"' +
                ", \"status\":\"" + status + '\"' +
                ", \"putawayTime\":\"" + putawayTime + '\"' +
                ", \"availableTime\":\"" + availableTime + '\"' +
                ", \"operation\":\""+operation+'\"'+
                ", \"price\":\"" + price + '\"' +
                ", \"address\":\"" + address + '\"' +
                ", \"paytype\":\"" + paytype + '\"' +
                ", \"type\":" + types +
                '}';
    }

    public String getInfo() {
        String tt = "";
        String d = "";
        for (String  t:this.type
             ) {
            tt+=d+t;
            d=",";
        }
        return "id=" + String.valueOf(id) +
                ", name=" + name +
                ", status=" + status +
                ", paytype={" + paytype +"}"+
                //", type=" + type  +
                ", putawayTime=" + putawayTime +
                ", availableTime=" + availableTime+
                ", price="+price+
                ", operation="+operation+
                ",address="+address+
                ",type={"+tt+"}";

    }

}
