package com.blasco991.chatapp.view;

import android.content.Context;
import android.support.annotation.UiThread;
import android.widget.Toast;

/**
 * Created by blasco991 on 11/04/17.
 */
public interface View {
    @UiThread
    void onModelChanged();

    @UiThread
    Context getContext();

    @UiThread
    default void makeToast(Context context, String text, int length) {
        Toast.makeText(context, text, length).show();
    }

    @UiThread
    default void makeToast(Context context, String text) {
        makeToast(context, text, Toast.LENGTH_SHORT);
    }
}
