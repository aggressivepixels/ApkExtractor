package com.jonathan.apkextractor.util;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.widget.TextView;

public class ViewUtils {

    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(View view, int id) {
        return (T) view.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }

    public static void setText(View view, String string, int id) {
        ((TextView) findViewById(view, id)).setText(string);
    }

    public static void fadeIn(View view) {
        ViewCompat.animate(view).alpha(1f).setDuration(200).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(View view) {

            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).start();
    }

    public static void fadeOut(View view) {
        ViewCompat.animate(view).alpha(0f).setDuration(200).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).start();
    }
}
