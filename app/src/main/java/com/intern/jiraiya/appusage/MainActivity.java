package com.intern.jiraiya.appusage;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.os.Process.myUid;
import static android.support.v4.app.AppOpsManagerCompat.MODE_ALLOWED;


public class MainActivity extends AppCompatActivity
//        implements AdapterView.OnItemSelectedListener
{

    public static final String TAG = "App_Usage";
//    private static final boolean localLOGV = false;
    public static UsageStatsManager mUsageStatsManager;
   // private LayoutInflater mInflater;
  //  private UsageStatsAdapter mAdapter;
  //  private PackageManager mPm;
    private MyUsageStats usage;
    public static List<UsageStats> stats;
    List<UsageStats> list = new ArrayList<>();

    //private ViewPager viewPager;
     RecyclerView recyclerView ;
     ActivitiesAdapter adapter;
    String[] dates= {"Last 24 hours","Last 48 hours","Last 72 hours","Last 96 hours","Last 120 hours","Average in 7 Days"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
     //   mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        recyclerView = (RecyclerView)findViewById(R.id.recycler) ;

        adapter = new ActivitiesAdapter(MainActivity.this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);


    //    ListView listView = (ListView) findViewById(R.id.pkg_list);
     //   mAdapter = new UsageStatsAdapter();
     //   listView.setAdapter(mAdapter);
        getTime(-1,0);
        getStaticSchedule();

//        int j=0;
//        for(int i=0; i > -6;i--){
//            Calendar cal1 = Calendar.getInstance();
//            Calendar cal2 = Calendar.getInstance();
//            cal2.add(Calendar.DATE,i-1);
//            cal1.add(Calendar.DATE,i);
//            String st1 = cal2.getTime().toString();
//            String st2 = cal1.getTime().toString();
//            st1=st1.substring(0,st1.length()-24);
//            st2=st2.substring(0,st2.length()-24);
//            Log.d(TAG,"Time "+ st1+" - "+st2);
//            dates[j] = st1+" - "+st2;
//            j++;
//        }



        Spinner spin = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dates);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Pos "+position, Toast.LENGTH_SHORT).show();

                if(position == 0)
                    getTime(-1,0);//21-22
                else if(position == 1)
                    getTime(-2,-1);//20-
                else if(position == 2)
                    getTime(-3,-2);
                else if(position == 3)
                    getTime(-4,-3);
                else if(position == 4)
                    getTime(-5,-4);
                else if(position == 5)
                    getTime(-7,-7);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!checkForPermission(MainActivity.this))
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        else {

            getTime(-1,0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

       clearList();
    }

    private void getStaticSchedule() {

        usage = new MyUsageStats(MainActivity.this,0,System.currentTimeMillis());
        stats = usage.getUsageList();

    }

    void clearList(){
        int size = list.size();
        list.clear();
        adapter.notifyItemRangeRemoved(0,size);
    }


    private void getTime(int start,int finish){

        clearList();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();


        cal1.add(Calendar.DATE, start);//15
        cal2.add(Calendar.DATE, finish);//16<-------



        Log.d(TAG,"Date = "+ cal1.getTime());
        Date date1 = cal1.getTime();
        long time1 = date1.getTime();
        Log.d(TAG,"Timestamp "+new Timestamp(time1).getTime());

        Log.d(TAG,"Date = "+ cal2.getTime());
        Date date2 = cal2.getTime();
        long time2 = date2.getTime();
        Log.d(TAG,"Timestamp "+new Timestamp(time2).getTime());



        int i=0;
        MyUsageStats usage2;
        if(start == -7) {
            usage2 = new MyUsageStats(MainActivity.this, time1, System.currentTimeMillis());
        }
        else
            usage2 = new MyUsageStats(MainActivity.this, time1, time2);

        for(android.app.usage.UsageStats st: usage2.getPkArray()) {

            list.add(st);
            adapter.notifyItemInserted(i);
            i++;

            Log.d(TAG,"---------------------------------------------------------------------------");
            Log.d(TAG, st.getPackageName());
            Log.d(TAG, "FirstTimeStamp N "+new Date(st.getFirstTimeStamp()));
            Log.d(TAG, "FirstTimeStamp N "+ DateUtils.formatDateTime(MainActivity.this,st.getFirstTimeStamp(),2));
            Log.d(TAG, "LastTimeStamp N "+new Date(st.getLastTimeStamp()));
            Log.d(TAG, "LastTimeUsed N "+new Date(st.getLastTimeUsed()));
           // Log.d(TAG, "ForegroundTimeUsed N "+new Date(st.getTotalTimeInForeground()));
            Log.d(TAG, "TotalTimeInForeground N "+DateUtils.formatElapsedTime(st.getTotalTimeInForeground()/1000));
            Log.d(TAG,"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        }

    }

    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), context.getPackageName());
        return mode == MODE_ALLOWED;
    }


}
