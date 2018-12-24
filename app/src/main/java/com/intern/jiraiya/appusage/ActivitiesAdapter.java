package com.intern.jiraiya.appusage;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.DateFormat;
import java.util.List;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<UsageStats> list;
    private Context context;
    private PackageManager mPm;

    public ActivitiesAdapter(Context context,List<UsageStats> list)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        mPm = context.getPackageManager();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = inflater.inflate(R.layout.items,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        UsageStats item = list.get(i);

        try {
            viewHolder.imageView.setImageDrawable( context.getPackageManager().getApplicationIcon(item.getPackageName()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        viewHolder.pkg_name.setText(item.getPackageName());
        viewHolder.last_time.setText((DateUtils.formatSameDayTime(item.getLastTimeUsed(),
                System.currentTimeMillis(), DateFormat.DATE_FIELD, DateFormat.MEDIUM)));

        try {
            ApplicationInfo appInfo = mPm.getApplicationInfo(item.getPackageName(), 0);
            viewHolder.app_name.setText(appInfo.loadLabel(mPm).toString());
        }
        catch(PackageManager.NameNotFoundException e) {
        // This package may be gone.
        }

        viewHolder.time.setText(DateUtils.formatElapsedTime(item.getTotalTimeInForeground()/1000));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goTest = new Intent(context,TestActivity.class);
                goTest.putExtra("PKG_NAME",viewHolder.pkg_name.getText().toString());
                context.startActivity(goTest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView last_time;
        TextView pkg_name;
        ImageView imageView;
        TextView app_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = (TextView)itemView.findViewById(R.id.time);
            last_time = (TextView)itemView.findViewById(R.id.last_time);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            pkg_name = (TextView)itemView.findViewById(R.id.pk_name);
            app_name = (TextView)itemView.findViewById(R.id.app_name);
        }
    }
}
