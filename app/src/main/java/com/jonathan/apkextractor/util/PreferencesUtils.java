package com.jonathan.apkextractor.util;

import android.content.Context;
import android.preference.PreferenceManager;

public class PreferencesUtils {

    private static final String PREF_KEY_SHOW_SYSTEM_APPS = "pref_key_show_system_apps";

    public static boolean showSystemApps(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_KEY_SHOW_SYSTEM_APPS, false);
    }

    public static void setShowSystemApps(Context context, boolean showSystemApps) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_KEY_SHOW_SYSTEM_APPS, showSystemApps).apply();
    }
}
