package com.example.attendanceapp;

public class ClassItem {

    String className;
    String subName;
    long cid;

    public ClassItem(long cid, String className, String subName) {
        this.className = className;
        this.subName = subName;
        this.cid = cid;
    }

    public ClassItem(String className, String subName) {
        this.className = className;
        this.subName = subName;
    }
    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }
}
