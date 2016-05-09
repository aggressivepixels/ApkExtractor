package com.apkextractor.android.common.utils;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import java.util.Locale;

/**
 * @author Jonathan Hernández
 */
public class RtlUtils {


    public static boolean isRtl() {
        final Locale locale = Locale.getDefault();
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && TextUtils.getLayoutDirectionFromLocale(locale) == View.LAYOUT_DIRECTION_RTL;
    }

    public static int getPositionForTextDirection(int position, int count) {
        if (isRtl()) {
            return count - 1 - position;
        }
        return position;
    }
}
