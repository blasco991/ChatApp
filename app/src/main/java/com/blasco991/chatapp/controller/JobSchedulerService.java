package com.blasco991.chatapp.controller;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.blasco991.chatapp.ChatApp;
import com.blasco991.chatapp.MVC;
import com.blasco991.chatapp.view.MainActivity;

/**
 * Created by marian on 26/04/2017.
 */

public class JobSchedulerService extends JobService {
    private MVC mvc;
    private static final String TAG = JobSchedulerService.class.getName();


    @Override
    public void onCreate() {
        super.onCreate();
        mvc = ((ChatApp) getApplication()).getMVC();
    }

    private Handler mJobHandler = new Handler(msg -> {
        Log.d(TAG, "JobService task running: " + this + "\tmsg: " + msg);
        mvc.controller.receiveMessages();
        jobFinished((JobParameters) msg.obj, false);
        return true;
    });

    @Override
    public boolean onStartJob(JobParameters params) {
        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, params));
        Log.d(TAG, "JobService startJob: " + this + "\tjobHandler: " + mJobHandler);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages(1);
        return false;
    }

}