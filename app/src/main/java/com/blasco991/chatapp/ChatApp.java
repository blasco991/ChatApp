package com.blasco991.chatapp;

import android.app.Application;
import android.widget.TextView;

import com.blasco991.chatapp.controller.Controller;
import com.blasco991.chatapp.model.Model;

/**
 * Created by blasco991 on 11/04/17.
 */

public class ChatApp extends Application {
    private MVC mvc;

    @Override
    public void onCreate() {
        super.onCreate();
        mvc = new MVC(new Model(), new Controller());
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
