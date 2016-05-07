package com.apkextractor.android.observer;

import android.content.SharedPreferences;

import com.apkextractor.android.loader.ApkFilesListLoader;
import com.apkextractor.android.util.PreferencesUtils;

public class ApkFilesSortOrderObserver implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ApkFilesListLoader mLoader;

    public ApkFilesSortOrderObserver(ApkFilesListLoader loader) {
        mLoader = loader;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferencesUtils.PREF_KEY_APK_FILES_LIST_SORT_BY)) {
            mLoader.onContentChanged();
        }
    }
}
