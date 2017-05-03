package com.blasco991.chatapp;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.blasco991.chatapp.controller.Controller;
import com.blasco991.chatapp.controller.JobService;
import com.blasco991.chatapp.model.Model;

import static android.app.job.JobInfo.NETWORK_TYPE_NOT_ROAMING;

/**
 * Created by blasco991 on 11/04/17.
 */

public class ChatApp extends Application {
    public static final int PERIODIC_JOB_TAG = 991;
    public static final int ONESHOT_JOB_TAG = 992;
    public static final long REFRESH_INTERVAL = 1000 * 60 * 10;

    private MVC mvc;
    private static final String TAG = ChatApp.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        mvc = new MVC(new Model(), new Controller());

        JobInfo.Builder builder = new JobInfo.Builder(PERIODIC_JOB_TAG, new ComponentName(this, JobService.class.getName()));
        builder.setPeriodic(REFRESH_INTERVAL).setRequiredNetworkType(NETWORK_TYPE_NOT_ROAMING)
                .setRequiresCharging(true).setPersisted(true);

        int scheduleResult = ((JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE)).schedule(builder.build());
        Log.d(TAG, "Schedule result: " + scheduleResult);

    }

    public MVC getMVC() {
        return mvc;
    }

    public static class MessageHolder {
        public int position;
        public TextView author;
        public TextView body;
    }

    public static class Message {
        public int position;
        public String author;
        public String body;
    }
}
