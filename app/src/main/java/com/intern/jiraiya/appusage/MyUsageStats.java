package com.intern.jiraiya.appusage;

import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.intern.jiraiya.appusage.MainActivity.mUsageStatsManager;

public class MyUsageStats {

    private final ArrayMap<String, String> mAppLabelMap = new ArrayMap<>();
    private final List<android.app.usage.UsageStats> mPackageStats = new ArrayList<>();
    private PackageManager mPm;
    private static final boolean localLOGV = false;
    List<android.app.usage.UsageStats> stats;
    private static final String TAG="MyUsageStats";
    private Context context;
    AppNameComparator mAppLabelComparator;
    private LastTimeUsedComparator mLastTimeUsedComparator = new LastTimeUsedComparator();
    private static final int _DISPLAY_ORDER_USAGE_TIME = 0;
    private static final int _DISPLAY_ORDER_LAST_TIME_USED = 1;
    private static final int _DISPLAY_ORDER_APP_NAME = 2;
    private int mDisplayOrder;
    private UsageTimeComparator mUsageTimeComparator = new UsageTimeComparator();


    MyUsageStats(Context context,long startTS,long endTS){

        mDisplayOrder = _DISPLAY_ORDER_USAGE_TIME;
        this.context = context;
        mPm = context.getPackageManager();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -5);

        stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                startTS,endTS);

        if (stats == null) {
            return;
        }



        ArrayMap<String, android.app.usage.UsageStats> map = new ArrayMap<>();

        final int statCount = stats.size();
        for (int i = 0; i < statCount; i++) {
            final android.app.usage.UsageStats pkgStats = stats.get(i);

            // load application labels for each application
            try {
                ApplicationInfo appInfo = mPm.getApplicationInfo(pkgStats.getPackageName(), 0);
                String label = appInfo.loadLabel(mPm).toString();
                mAppLabelMap.put(pkgStats.getPackageName(), label);

                android.app.usage.UsageStats existingStats =
                        map.get(pkgStats.getPackageName());
                if (existingStats == null) {
                    map.put(pkgStats.getPackageName(), pkgStats);
                } else {
                    existingStats.add(pkgStats);
                }

            } catch (PackageManager.NameNotFoundException e) {
                // This package may be gone.
            }
        }
        mPackageStats.addAll(map.values());

        // Sort list
        mAppLabelComparator = new AppNameComparator(mAppLabelMap);
        sortList();
    }


    List<android.app.usage.UsageStats> getUsageList(){

        return stats;
    }

    List<android.app.usage.UsageStats> getPkArray(){

        return mPackageStats;
    }

    private void sortList() {
        if (mDisplayOrder == _DISPLAY_ORDER_USAGE_TIME) {
            if (localLOGV) Log.i(TAG, "Sorting by usage time");
            Collections.sort(mPackageStats, mUsageTimeComparator);
        } else if (mDisplayOrder == _DISPLAY_ORDER_LAST_TIME_USED) {
            if (localLOGV) Log.i(TAG, "Sorting by last time used");
            Collections.sort(mPackageStats, mLastTimeUsedComparator);
        } else if (mDisplayOrder == _DISPLAY_ORDER_APP_NAME) {
            if (localLOGV) Log.i(TAG, "Sorting by application name");
            Collections.sort(mPackageStats, mAppLabelComparator);
        }

    }




}
