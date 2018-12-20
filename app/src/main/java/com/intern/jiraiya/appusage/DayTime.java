package com.intern.jiraiya.appusage;

public class DayTime {
    private String day;
    private String time;
    private long min;

    public DayTime(String day, String time, long min) {
        this.day = day;
        this.time = time;
        this.min = min;
    }

    @Override
    public String toString() {
        return "DayTime{" +
                "day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", min=" + min +
                '}';
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }
}
