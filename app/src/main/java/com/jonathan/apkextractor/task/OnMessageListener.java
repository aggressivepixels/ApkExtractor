package com.jonathan.apkextractor.task;

import android.view.View;

public interface OnMessageListener {

    void onMessage(String message);

    void onMessage(String message, String buttonText, View.OnClickListener onClickListener);
}
