package com.blasco991.chatapp;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import com.blasco991.chatapp.controller.Controller;
import com.blasco991.chatapp.controller.JobSchedulerService;
import com.blasco991.chatapp.model.Model;

/**
 * Created by blasco991 on 11/04/17.
 */

public class ChatApp extends Application {
    private static final long REFRESH_INTERVAL = 60000*15;
    private MVC mvc;
    private static final String TAG = ChatApp.class.getName();
    private static final int JOB_TAG = 991;

    @Override
    public void onCreate() {
        super.onCreate();
        mvc = new MVC(new Model(), new Controller());

        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_TAG, new ComponentName(getPackageName(), JobSchedulerService.class.getName()));

        builder.setPeriodic(REFRESH_INTERVAL);

        int scheduleResult;
        if ((scheduleResult = mJobScheduler.schedule(builder.build())) != JobScheduler.RESULT_SUCCESS) {
            Log.e(TAG, "Schedule result: " + scheduleResult);
        }
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
