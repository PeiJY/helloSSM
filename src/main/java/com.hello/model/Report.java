package com.hello.model;

import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author Pei Jiyuan
 * date 2019/4/27
 * desc
 */


public class Report {
    private long reportid;
    private String reason;
    private String date;
    private long objectid;
    private long reporterid;

    public long getReporterid() {
        return reporterid;
    }

    public void setReporterid(long reporterid) {
        this.reporterid = reporterid;
    }

    public Report(String reason, long objectid, long reporterid) {
        this.reason = reason;
        this.objectid = objectid;
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        this.date =  sdf.format(d);
        this.reporterid=reporterid;
    }
    public Report() {}

    public long getReportid() {
        return reportid;
    }

    public void setReportid(long reportid) {
        this.reportid = reportid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getObjectid() {
        return objectid;
    }

    public void setObjectid(long objectid) {
        this.objectid = objectid;
    }


    public String toString(String reportname) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reportid",String.valueOf(reportid));
        jsonObject.put("reason", reason);
        jsonObject.put("date", date);
        jsonObject.put("reportName", reportname);
        jsonObject.put("objectid", String.valueOf(objectid));
        return jsonObject.toString();
    }
}
