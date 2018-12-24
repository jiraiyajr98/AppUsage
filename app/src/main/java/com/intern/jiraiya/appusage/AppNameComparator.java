package com.intern.jiraiya.appusage;

import android.app.usage.UsageStats;

import java.util.Comparator;
import java.util.Map;

public class AppNameComparator implements Comparator<UsageStats> {
    private Map<String, String> mAppLabelList;

    AppNameComparator(Map<String, String> appList) {
        mAppLabelList = appList;
    }

    @Override
    public final int compare(UsageStats a, UsageStats b) {
        String alabel = mAppLabelList.get(a.getPackageName());
        String blabel = mAppLabelList.get(b.getPackageName());
        return alabel.compareTo(blabel);
    }
}
