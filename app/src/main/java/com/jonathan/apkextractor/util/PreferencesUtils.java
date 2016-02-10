package com.jonathan.apkextractor.util;

import android.content.Context;
import android.preference.PreferenceManager;

public class PreferencesUtils {

    //Preference keys
    public static final String PREF_KEY_SHOW_SYSTEM_APPS = "pref_key_show_system_apps";
    public static final String PREF_KEY_APK_FILES_LIST_SORT_BY = "apk_files_list_sort_by";

    //Sort types for the apk files list
    public static final int SORT_BY_DATE = 0;
    public static final int SORT_ALPHABETICALLY = 1;

    public static boolean showSystemApps(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_KEY_SHOW_SYSTEM_APPS, false);
    }

    public static void setShowSystemApps(Context context, boolean showSystemApps) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_KEY_SHOW_SYSTEM_APPS, showSystemApps).apply();
    }

    public static int getApkFilesListSortOrder(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_KEY_APK_FILES_LIST_SORT_BY, SORT_BY_DATE);
    }

    public static void setApkFilesListSortOrder(Context context, int sortType) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(PREF_KEY_APK_FILES_LIST_SORT_BY, sortType).apply();
    }
}
