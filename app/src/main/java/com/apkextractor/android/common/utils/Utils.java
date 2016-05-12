package com.apkextractor.android.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

public class Utils {
    /**
     * A helper method for showing the app's settings screen.
     *
     * @param activity An activity instance.
     * @return Whether the app settings were opened or not.
     */
    public static boolean showAppSettingsScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts(
                            "package",
                            activity.getPackageName(),
                            null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }
}
