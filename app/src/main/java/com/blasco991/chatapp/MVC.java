package com.blasco991.chatapp;


import com.blasco991.chatapp.controller.Controller;
import com.blasco991.chatapp.model.Model;
import com.blasco991.chatapp.view.View;

import net.jcip.annotations.ThreadSafe;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by blasco991 on 11/04/17.
 */
@ThreadSafe
public class MVC {
    public final Model model;
    public final Controller controller;
    private final List<View> views = new CopyOnWriteArrayList<>();

    public MVC(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
        model.setMVC(this);
        controller.setMVC(this);
    }

    public void register(View view) {
        views.add(view);
    }

    public void unregister(View view) {
        views.remove(view);
    }

    public interface ViewTask {
        void process(View view);
    }

    public void forEachView(ViewTask task) {
        for (View view : views)
            task.process(view);
    }
}