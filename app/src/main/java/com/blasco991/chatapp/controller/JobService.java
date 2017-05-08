package com.blasco991.chatapp.controller;

import android.app.job.JobParameters;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.blasco991.chatapp.ChatApp;
import com.blasco991.chatapp.MVC;

import static com.blasco991.chatapp.ChatApp.ONESHOT_JOB_TAG;
import static com.blasco991.chatapp.ChatApp.PERIODIC_JOB_TAG;

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

    private Handler mJobHandler = new Handler(msg -> {
        Log.d(TAG, "JobService task running: " + Thread.currentThread() + "\tmsg: " + msg);
        mvc.controller.receiveMessages();
        jobFinished((JobParameters) msg.obj, false);
        return true;
    });

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "JobService startJob: " + Thread.currentThread() + "\tJobID: " + params.getJobId());
        mJobHandler.sendMessage(Message.obtain(mJobHandler, params.getJobId(), params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages(ONESHOT_JOB_TAG);
        mJobHandler.removeMessages(PERIODIC_JOB_TAG);
        return false;
    }

}