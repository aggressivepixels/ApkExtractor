package com.apkextractor.android.util;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.widget.TextView;

public class ViewUtils {

    public static final int DEFAULT_ANIMATION_DURATION = 200;

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
        if (view != null) {
            ViewCompat.animate(view).alpha(1f).setDuration(DEFAULT_ANIMATION_DURATION).setListener(new ViewPropertyAnimatorListener() {
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
    }

    public static void fadeOut(View view) {
        if (view != null) {
            ViewCompat.animate(view).alpha(0f).setDuration(DEFAULT_ANIMATION_DURATION).setListener(new ViewPropertyAnimatorListener() {
                @Override
                public void onAnimationStart(View view) {

                }

                @Override
                public void onAnimationEnd(View view) {
                    ViewCompat.setAlpha(view, 1f);
                    view.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(View view) {

                }
            }).start();
        }
    }
}
