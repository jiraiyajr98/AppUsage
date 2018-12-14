package com.intern.jiraiya.appusage;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.intern.jiraiya.appusage.MainActivity.stats;

public class TestActivity extends AppCompatActivity {


    private static final String TAG = "TEST_ACTIVITY";
    private List<ActivitiesList> list = new ArrayList<>();
    private boolean go = true;
    RecyclerView recyclerView ;
    ActivitiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        String pkg_name = getIntent().getStringExtra("PKG_NAME");

        recyclerView = (RecyclerView)findViewById(R.id.rv) ;
        adapter = new ActivitiesAdapter(TestActivity.this,list);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TestActivity.this));

        Log.d(TAG,pkg_name);
        getDetails(pkg_name);
    }

    void getDetails(String pkg_name)
    {
        int i=0;
        for(UsageStats us:stats)
        {
            if(us.getPackageName().equals(pkg_name))
            {

                String day = DateUtils.formatDateTime(TestActivity.this,us.getFirstTimeStamp(),2);
                String time = DateUtils.formatElapsedTime(us.getTotalTimeInForeground()/1000);


                for(ActivitiesList l:list)
                {
                    if(l.getApp_name().equals(day))
                    {
                        Log.d(TAG,"Name "+l.getApp_name()+" Time "+l.getTime());
                        //l.setTime(addTime(l.getTime(),time));
                        go = false;
                        break;
                    }
                    else
                        go = true;
                }
                if(go) {
                    list.add(new ActivitiesList(day, time));
                    adapter.notifyItemInserted(i);
                    i++;
                }


            }

        }

        Log.d(TAG,"Size "+list.size());
        for(ActivitiesList l:list) {
            Log.d(TAG,"--------------------------------");
            Log.d(TAG, "Day " + l.getApp_name()+" Time "+l.getTime());
            Log.d(TAG, "--------------------------------");
        }

    }


}
