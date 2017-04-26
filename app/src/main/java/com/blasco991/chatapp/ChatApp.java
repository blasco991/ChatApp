package com.blasco991.chatapp;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.blasco991.chatapp.controller.Controller;
import com.blasco991.chatapp.controller.JobSchedulerService;
import com.blasco991.chatapp.model.Model;

/**
 * Created by blasco991 on 11/04/17.
 */

public class ChatApp extends Application {
    private MVC mvc;
    private JobScheduler mJobScheduler;
    private static final String TAG = ChatApp.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        mvc = new MVC(new Model(), new Controller());

        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(1,
                new ComponentName(getPackageName(), JobSchedulerService.class.getName()));

        builder.setPeriodic(3600000);

        int scheduleResult;
        if ((scheduleResult = mJobScheduler.schedule(builder.build())) <= 0) {
            Log.d(TAG, "Schedule result" + scheduleResult);
        }
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
