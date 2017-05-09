package com.blasco991.chatapp.controller;

import android.app.job.JobParameters;
import android.util.Log;

import com.blasco991.chatapp.ChatApp;
import com.blasco991.chatapp.MVC;

/**
 * Created by marian on 26/04/2017.
 */

public class JobService extends android.app.job.JobService {
    private MVC mvc;
    private static final String TAG = JobService.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        mvc = ((ChatApp) getApplication()).getMVC();
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "JobService startJob on ThreadID: " + Thread.currentThread().getId() + "\tJobID: " + params.getJobId());
        new Thread(() -> {
            Log.d(TAG, "JobService task running on ThreadID: " + Thread.currentThread().getId() + "\tparams: " + params);
            mvc.controller.receiveMessages();
            jobFinished(params, false);
        }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}