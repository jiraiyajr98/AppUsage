package com.intern.jiraiya.appusage;

public class ActivitiesList {

    private String app_name;
    private String time;
    private String packageName;

    public ActivitiesList(String app_name, String time, String packageName) {
        this.app_name = app_name;
        this.time = time;
        this.packageName = packageName;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
