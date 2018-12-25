package com.hello.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * project helloSSM
 * authod wuyanhui
 * datetime 2017/11/23 16:17
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

    @Override
    public String toString() {
        return "Report{" +
                "reportid=" + reportid +
                ", reason='" + reason + '\'' +
                ", date='" + date + '\'' +
                ", objectid=" + objectid +
                ", reporterid=" + reporterid +
                '}';
    }

    public String returnMassage(String reportname){
        return "{"+
                "\"reportid\":\"" + String.valueOf(reportid) + '\"' +
                ",\"reason\":\"" + reason + '\"' +
                ",\"date\":\"" + date + '\"' +
                ",\"reportName\":\"" + reportname + '\"' +
                ",\"objectid\":\""+ String.valueOf(objectid)+"\""+"}";
    }
}
