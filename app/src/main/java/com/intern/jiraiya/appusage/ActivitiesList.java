package com.intern.jiraiya.appusage;

public class ActivitiesList {

    private String app_name;
    private String time;

    public ActivitiesList(String app_name, String time) {
        this.app_name = app_name;
        this.time = time;
    }

    public String getApp_name() {
        return app_name;
    }

    public String getTime() {
        return time;
    }
}