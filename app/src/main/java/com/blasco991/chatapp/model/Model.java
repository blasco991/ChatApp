package com.blasco991.chatapp.model;

import android.support.annotation.UiThread;

import com.blasco991.chatapp.MVC;
import com.blasco991.chatapp.view.View;

import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by blasco991 on 11/04/17.
 */

@ThreadSafe
public class Model {
    private MVC mvc;
    private List<Message> messages;

    public void setMVC(MVC mvc) {
        this.mvc = mvc;
        this.messages = new LinkedList<>();
    }

    @UiThread
    public void setMessages(List<Message> messeges) {
        this.messages.clear();
        this.messages.addAll(messeges);
        mvc.forEachView(View::onModelChanged);
    }

    @UiThread
    public List<Message> getMessages() {
        return messages;
    }


    public static class Message {
        public int position;
        public String author;
        public String body;
    }

}
