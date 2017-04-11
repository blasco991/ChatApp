package com.blasco991.chatapp.view;

import android.support.annotation.UiThread;

/**
 * Created by blasco991 on 11/04/17.
 */
public interface View {
    @UiThread
    void onModelChanged();

    @UiThread
    void makeToast(String text);

    @UiThread
    void makeToast(String text, int length);
}
