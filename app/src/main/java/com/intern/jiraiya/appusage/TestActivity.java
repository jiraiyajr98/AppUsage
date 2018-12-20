package com.intern.jiraiya.appusage;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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
    private ArrayList<DayTime> list = new ArrayList<>();
    private boolean go = true;
   // RecyclerView recyclerView ;
  //  ActivitiesAdapter adapter;
    boolean isPresent;
    PieChart piechart;
    List<PieEntry> data = new ArrayList<>();
    ImageView iconImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        String pkg_name = getIntent().getStringExtra("PKG_NAME");

        piechart = (PieChart)findViewById(R.id.chart);
        piechart.setDragDecelerationFrictionCoef(0.99f);
        iconImg = findViewById(R.id.icon_image);

        try
        {
            Drawable icn = getPackageManager().getApplicationIcon(pkg_name);
            iconImg.setImageDrawable(icn);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

     //   recyclerView = (RecyclerView)findViewById(R.id.rv) ;
      //  adapter = new ActivitiesAdapter(TestActivity.this,list);

     //   recyclerView.setAdapter(adapter);
      //  recyclerView.setLayoutManager(new LinearLayoutManager(TestActivity.this));

        Log.d(TAG,pkg_name);
        getDetails(pkg_name);
    }

    void getDetails(String pkg_name)
    {
        long min;
        int i=0;
        for(UsageStats us:stats)
        {

            if(us.getPackageName().equals(pkg_name))
            {
               // Log.d(TAG, "All Day "+DateUtils.formatDateTime(TestActivity.this, us.getFirstTimeStamp(), 2));
                if(i != 0) {
                    Log.d(TAG, "--------------------------------");
                    String day = DateUtils.formatDateTime(TestActivity.this, us.getFirstTimeStamp(), 2);
                    String time = DateUtils.formatElapsedTime(us.getTotalTimeInForeground() / 1000);
                    Log.d(TAG, "Day " + day + " Time " + time + " Min " + us.getTotalTimeInForeground() / 1000);
                    Log.d(TAG, "--------------------------------");
                    min = us.getTotalTimeInForeground() / 1000;
                    DayTime dt = new DayTime(day, time, min);
                    if (!replication(dt)) {
                        dt.setTime(convrt_date(dt.getMin()));
                        list.add(dt);
                    }
                }
                else
                    i++;
            }

        }
        for(DayTime dt:list){
            Log.d(TAG,"DT_LIST"+dt.toString());
            data.add(new PieEntry(dt.getMin(),dt.getDay()+" "+dt.getTime()));
        }

        PieDataSet dataSet = new PieDataSet(data,"App Usage");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData pieData = new PieData(dataSet);
        piechart.setData(pieData);


    }

    boolean replication(DayTime dt){

        for(DayTime d:list)
        {
            if(d.getDay().equals(dt.getDay()))
            {
                long time = dt.getMin()+d.getMin();
                d.setTime(convrt_date(time));
                d.setMin(time);
                return true;

            }
        }
        return false;

    }

    String convrt_date(long m){

        int hours = (int)m/3600;
        int temM = (int)m%3600;
        int min = temM/60;

        if(hours <= 0)
            return min+" minuets";
        else
            return hours+"h "+min+" minuets";

    }



}
